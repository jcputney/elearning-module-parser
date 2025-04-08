# Server-Side Handling and Launching of SCORM 2004 Modules with Sequencing

## 1. Introduction

SCORM 2004 (1st through 4th Edition) introduced a **Sequencing and Navigation** (SN) model that
dictates how content (SCOs) should be traversed. This guide covers the **server-side
responsibilities** of an LMS when handling SCORM 2004 modules—specifically addressing the question:

> “What does the LMS have to get out of the module, and what does it need to send to the browser
> when launching a module?”

We’ll examine:

- How the LMS must **import** the SCORM package and store its data (particularly sequencing info).
- What **runtime data** the LMS must generate and deliver to the browser/SCO at launch.
- How the LMS manages **sequencing context**, activity attempts, and resume state—across all SCORM
  2004 editions.

**Note**: Code examples are conceptual; we’ll use Java for illustration but keep discussion
language-neutral.

---

## 2. SCORM 2004 Sequencing Overview

SCORM 2004 structures a course into a hierarchical **activity tree**, defined by the
`imsmanifest.xml`. Each activity may have:

- **Sequencing rules**: precondition, postcondition, exit actions, etc.
- **Control modes**: choice/flow, forwardOnly.
- **Limit conditions**: attempt limits, time limits.
- **Rollup rules** for parent activities.
- **Objectives**: local or shared (global).
- **(4th Ed)** Weighted rollup, shared data buckets.

During runtime, the LMS enforces these rules to determine **which SCO** (leaf activity) a learner
sees next.

---

## 3. Import-Time Responsibilities

When the LMS imports a SCORM 2004 package:

1. **Parse the Manifest and Build the Activity Tree**
   - Each `<item>` in `<organization>` becomes an activity node.
   - Store the activity **identifier**, parent/child hierarchy, and reference to `<resource>` for
     leaf SCOs.

2. **Extract Launchable SCO Info**
   - **Launch URL** (from `<resource href="...">`) and any parameters.
   - `<adlcp:dataFromLMS>` if present—stored for eventual runtime (`cmi.launch_data`).
   - Optional mastery score references (if `<adlcp:masteryscore>` or `<imsss:objective>` with
     thresholds are used).

3. **Store Sequencing Definitions**
   - **Control Modes** (choice, flow, forwardOnly).
   - **Precondition, Postcondition Rules** (skip, exit, retry, etc.).
   - **Limit Conditions** (attemptLimit, etc.).
   - **Objectives** (local vs global).
   - **Rollup Rules** (how child statuses aggregate).
   - **Randomization/Selection** rules if applicable.
   - **(4th Ed)** Mappings for shared data buckets (`<adlcp:data>` with `<adlcp:map>` elements).

4. **Edition Differences**
   - 1st–2nd Ed: basic definitions, no Jump, limited SuspendAll usage by SCOs.
   - 3rd Ed: refined specs, SCOs can explicitly invoke SuspendAll.
   - 4th Ed: adds **Jump** request, **weighted rollup** for progress, **shared data buckets**.

**Outcome**: The LMS has a **complete internal model** of the activity tree, resource URLs, and
sequencing rules—ready for runtime reference.

---

## 4. Server-Side Tracking Model

For each learner’s interaction with a SCORM 2004 course, the LMS maintains:

- **Activity status** (not attempted, incomplete, completed).
- **Success status** (passed, failed, unknown).
- **Score** (raw/scaled), progress measure.
- **Attempt count** (incremented each time a new attempt on that activity starts).
- **Suspend/Resume data** (`cmi.suspend_data`, `cmi.location`).
- **Global objectives** (shared statuses/scores across activities).
- **(4th Ed)** Shared data buckets if used.

This model is typically stored in a database so the LMS can **resume** between sessions. It
parallels SCORM’s `cmi.*` data model but on the server side.

---

## 5. Launching a SCO: What Goes to the Browser?

When a learner launches or continues a SCO:

1. **Identify the Target SCO**
   - Using sequencing logic or the user’s choice, the LMS picks which activity (leaf SCO) to
     deliver.

2. **Resolve the Launch URL**
   - Combine the SCO’s resource path with any parameters.
   - Possibly wrap it in an LMS-specific link (e.g., a servlet) that manages session tracking.

3. **Create/Update the Attempt**
   - If it’s a new attempt, increment attempt count, reset attempt-specific data.
   - If resuming, mark it as `cmi.entry="resume"` and restore stored suspend data.
   - Otherwise, use `"ab-initio"` (or empty) for `cmi.entry`.

4. **Serve an HTML/Frameset/Player Page**
   - **Important**: Provide the `API_1484_11` JavaScript object so the SCO can call `Initialize`,
     `GetValue`, etc.
   - The `<iframe>` or `<frame>` (or new window) loads the SCO’s start URL.
   - The parent/opener scope contains the SCORM API object.

5. **Initial Runtime Data** (available via API `GetValue`)
   - `cmi.entry` (`"resume"`, `"ab-initio"`, or `""`).
   - `cmi.suspend_data` / `cmi.location` if resuming.
   - `cmi.launch_data` (from manifest).
   - `cmi.credit` (usually `"credit"`).
   - `cmi.mode` (normally `"normal"`).
   - `cmi.objectives._count` and IDs if multiple objectives.
   - `adl.nav.request_valid.*` flags to indicate allowed navigation (continue/previous/choice/jump).

This ensures the SCO can **retrieve** the correct state (bookmark, dataFromLMS, etc.) and display
the appropriate content/lesson flow.

### Example (Java Pseudocode)

```java
public void launchSCO(HttpServletRequest req, HttpServletResponse resp) throws IOException {
   // 1. Determine the next SCO (sequencing engine / user action)
   Activity nextActivity = sequencingEngine.resolveNextActivity(currentActivity, navRequest);

   // 2. Resolve launch info
   String launchUrl = contentBasePath + nextActivity.getResource().getHref();

   // 3. Manage attempt
   Attempt attempt = loadOrCreateAttempt(userId, nextActivity);
   if (attempt.isNew()) {
      attempt.setEntry("ab-initio");
      attempt.incrementAttemptCount();
   } else if (attempt.isSuspended()) {
      attempt.setEntry("resume");
   }
   saveAttempt(attempt);

   // 4. Output frameset with SCORM API in the parent
   resp.getWriter().println("<html><head><script src='scorm_api_adapter.js'></script></head>");
   resp.getWriter()
       .println("<frameset><frame src='" + launchUrl + "' name='scoFrame'/></frameset></html>");
}
```

## 6. Runtime Communication and LMS Duties

Once the SCO is launched, it calls:

1. **`Initialize("")`**
   - The LMS marks the attempt as active and returns `"true"`.
   - This may involve noting a start timestamp for the session or confirming the previously
     determined attempt state (e.g., `entry="resume"` or `"ab-initio"`).

2. **`GetValue(...)` / `SetValue(...)`**
   - The SCO reads or writes runtime data. The LMS enforces SCORM 2004 data model rules (data types,
     read/write permissions, etc.).
   - Examples:
      - `GetValue("cmi.suspend_data")` → returns any stored suspend data.
      - `SetValue("cmi.location", "Page5")` → updates the learner’s bookmark for that SCO attempt.
      - `SetValue("cmi.completion_status", "completed")` → signals the LMS to mark the SCO as
        complete.
   - If the SCO tries to set a read-only element or invalid format, the LMS returns an error code.

3. **(Optional) `Commit("")`**
   - If the SCO calls `Commit`, the LMS ensures any pending changes are permanently saved.
   - Many modern LMS implementations save continuously (on each `SetValue` call) but still return
     `"true"` to indicate a successful commit.

4. **`Terminate("")`**
   - The SCO calls this to end its session. The LMS finalizes the attempt and triggers *
     *postcondition rules**.
   - Once terminated, further `SetValue` calls are invalid.
   - The LMS typically checks if `cmi.exit="suspend"` or normal exit, then updates attempt status
     accordingly (e.g., keep attempt open or finalize it).
   - After termination, the LMS decides if sequencing moves on to another SCO or if the course
     session ends.

Throughout this flow, the **server** must maintain current tracking data so it can apply sequencing
logic accurately when needed.

---

## 7. Applying Sequencing at the Server

After each SCO attempt ends or a navigation request is triggered:

1. **End Current Activity**
   - Mark the SCO’s attempt as complete/incomplete, passed/failed, or suspended (per final data from
     `SetValue` calls).

2. **Check Postcondition Rules**
   - e.g., “If `success_status` = failed, then `retry`” or “If completed, then `exitAll`.”
   - These can override the requested navigation path.

3. **Resolve the Next Activity**
   - For “Continue”/“Previous,” find the appropriate sibling in the activity tree.
   - For “Choice”/“Jump,” navigate to the targeted activity ID (if allowed by control modes).
   - Consider precondition rules that might skip or disable certain activities.

4. **Update Rollup**
   - If a child’s status changed, recalculate the parent’s status. e.g., if all children of a
     cluster are complete, the cluster is complete.
   - (4th Ed) Weighted rollup uses `cmi.progress_measure` from children to compute overall progress.

5. **Launch or Conclude**
   - If another SCO is selected, initiate that launch sequence (new attempt or resume).
   - If no activities remain or `exitAll` is triggered, the LMS ends the course session.

**Implementation**: Follow SCORM 2004’s official pseudocode to handle edge cases consistently,
ensuring correct transitions even in complex course structures.

---

## 8. Resuming a Suspended Course

When a course is **suspended** (e.g., via `SuspendAll` or abrupt closure with `cmi.exit="suspend"`):

- The LMS preserves the **entire tracking state** for each activity (completion, score,
  suspend_data, etc.).
- On next launch, the LMS sees that an attempt is suspended. It reassigns `cmi.entry="resume"` for
  that SCO and provides the previous `cmi.suspend_data`.
- This way, the learner returns exactly where they left off.
- Rollup statuses are also maintained, so any completed activities remain completed.
- If the learner restarts the course from scratch, the LMS may create a **new** attempt (clearing
  suspend_data), unless the design calls for resuming by default.

---

## 9. Key Edition-Specific Notes

1. **SCORM 2004 1st/2nd Edition**
   - No formal `jump` request; `SuspendAll` from SCO was limited or ambiguous.
   - Core sequencing structure is largely the same.

2. **SCORM 2004 3rd Edition**
   - Officially allows SCOs to call `adl.nav.request="suspendAll"` (SCO-initiated suspend).
   - Refines data model behavior and clarifies error codes.

3. **SCORM 2004 4th Edition**
   - Adds **`jump`** navigation request (bypassing normal choice constraints).
   - Weighted rollup for progress measure.
   - **Shared data buckets** for storing data across SCOs (`adl.data`).
   - More comprehensive handling of global objectives.
   - Backward-compatible with earlier editions.

If you implement the 4th Edition features, you effectively support all earlier SCORM 2004 content.

---

## 10. Summary

In summary, **server-side** handling of SCORM 2004 modules with sequencing entails:

1. **Import**: Extract activity tree, SCO resources, and all sequencing definitions (manifest).
2. **Tracking Model**: Maintain per-activity state (attempts, status, objectives, etc.) and global
   data.
3. **Launch**: Determine the SCO to deliver based on sequencing; set or resume attempt data (
   `cmi.entry`, `cmi.suspend_data`), and serve the **SCORM API** to the browser.
4. **Runtime**: Handle `Initialize`, `GetValue`, `SetValue`, `Commit`, `Terminate` calls in real
   time, updating tracking data.
5. **Sequencing**: Whenever an activity ends or nav request arises, evaluate rules (pre/post/exit),
   check rollup, and launch the next SCO or end the course.
6. **Resume**: If suspended, restore the entire state on next session, continuing from the last
   point.
7. **Edition Differences**: Implement 4th Ed features (jump, shared data, weighted rollup) for full
   compatibility.

Doing so ensures a **standards-compliant** SCORM 2004 experience, allowing content from any SCORM
2004 edition to run properly under your LMS’s server-side sequencing engine.