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
    resp.getWriter().println("<frameset><frame src='" + launchUrl + "' name='scoFrame'/></frameset></html>");
}