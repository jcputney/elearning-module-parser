package dev.jcputney.elearning.parser.api;

import dev.jcputney.elearning.parser.input.lom.LOM;

public interface LoadableMetadata {

  String getLocation();

  LOM getLom();

  void setLom(LOM lom);
}
