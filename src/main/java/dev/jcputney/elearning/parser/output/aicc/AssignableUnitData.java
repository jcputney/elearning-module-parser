package dev.jcputney.elearning.parser.output.aicc;

import java.util.List;

/**
 * Represents a collection of assignable units parsed from an AICC .au file.
 */
public class AssignableUnitData {

  private final List<AssignableUnit> assignableUnits;

  public AssignableUnitData(List<AssignableUnit> assignableUnits) {
    this.assignableUnits = assignableUnits;
  }

  public List<AssignableUnit> getAssignableUnits() {
    return assignableUnits;
  }
}
