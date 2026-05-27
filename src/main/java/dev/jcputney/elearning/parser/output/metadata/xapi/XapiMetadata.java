/*
 * Copyright (c) 2024-2026 Jonathan Putney
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the project root LICENSE file
 * or at http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package dev.jcputney.elearning.parser.output.metadata.xapi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import dev.jcputney.elearning.parser.enums.ModuleEditionType;
import dev.jcputney.elearning.parser.enums.ModuleType;
import dev.jcputney.elearning.parser.input.xapi.tincan.TincanActivity;
import dev.jcputney.elearning.parser.input.xapi.tincan.TincanManifest;
import dev.jcputney.elearning.parser.input.xapi.types.SimpleLangString;
import dev.jcputney.elearning.parser.output.ModuleMetadata;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * Metadata for xAPI/TinCan packaged modules.
 *
 * <p>TinCan packages contain a single activity and always have xAPI enabled.
 * They do not support multiple launchable units.</p>
 */
public class XapiMetadata extends ModuleMetadata<TincanManifest> {

  private static final String UNDETERMINED_LANGUAGE = "und";

  /**
   * The first activity id in the TinCan manifest. This is the launch activity id used by existing
   * {@link #getIdentifier()} behavior.
   */
  private String launchActivityId;

  /**
   * The first activity launch URL in the TinCan manifest. This is the launch URL used by existing
   * {@link #getLaunchUrl()} behavior.
   */
  private String launchActivityUrl;

  /**
   * The first activity type URI, if present.
   */
  private String activityType;

  /**
   * First activity names keyed by language tag.
   */
  private final Map<String, String> localizedNames = new LinkedHashMap<>();

  /**
   * First activity descriptions keyed by language tag.
   */
  private final Map<String, String> localizedDescriptions = new LinkedHashMap<>();

  /**
   * Protected no-argument constructor for XapiMetadata.
   * <p>
   * This constructor is primarily used for internal and subclassing purposes. It initializes an
   * instance of XapiMetadata without setting a manifest or other properties.
   * <p>
   * Instances created using this constructor are expected to be configured manually or used in
   * specific scenarios where the default state is sufficient.
   */
  protected XapiMetadata() {
  }

  /**
   * Constructor for XapiMetadata.
   *
   * @param manifest the TinCan manifest
   */
  public XapiMetadata(TincanManifest manifest) {
    super(manifest, ModuleType.XAPI, ModuleEditionType.XAPI, true);
    Optional
        .ofNullable(getFirstActivity(manifest))
        .ifPresent(activity -> {
          this.launchActivityId = activity.getId();
          this.launchActivityUrl = activity.getLaunch();
          this.activityType = activity.getType();
          this.localizedNames.putAll(toLanguageMap(activity.getNames()));
          this.localizedDescriptions.putAll(toLanguageMap(activity.getDescriptions()));
        });
  }

  private static TincanActivity getFirstActivity(TincanManifest manifest) {
    return Optional
        .ofNullable(manifest)
        .map(TincanManifest::getActivities)
        .filter(activities -> !activities.isEmpty())
        .map(activities -> activities.get(0))
        .orElse(null);
  }

  private static Map<String, String> toLanguageMap(List<SimpleLangString> values) {
    Map<String, String> languageMap = new LinkedHashMap<>();
    if (values == null) {
      return languageMap;
    }
    for (SimpleLangString value : values) {
      if (value != null && value.getValue() != null) {
        String lang = value.getLang();
        if (lang == null || lang.isBlank()) {
          lang = UNDETERMINED_LANGUAGE;
        }
        languageMap.put(lang, value.getValue());
      }
    }
    return languageMap;
  }

  /**
   * TinCan packages always have a single activity, so this always returns false.
   *
   * @return false
   */
  @Override
  public boolean hasMultipleLaunchableUnits() {
    return false;
  }

  /**
   * Gets the activities parsed from the TinCan manifest.
   *
   * @return an unmodifiable list of activities, or an empty list if no manifest is present
   */
  @JsonIgnore
  public List<TincanActivity> getActivities() {
    return Optional
        .ofNullable(manifest)
        .map(TincanManifest::getActivities)
        .map(List::copyOf)
        .orElseGet(List::of);
  }

  /**
   * Gets the launch activity id from the first TinCan activity.
   *
   * @return the launch activity id, or {@code null} if unavailable
   */
  public String getLaunchActivityId() {
    return launchActivityId;
  }

  /**
   * Sets the launch activity id.
   *
   * @param launchActivityId the launch activity id
   */
  public void setLaunchActivityId(String launchActivityId) {
    this.launchActivityId = launchActivityId;
  }

  /**
   * Gets the launch URL from the first TinCan activity.
   *
   * @return the launch activity URL, or {@code null} if unavailable
   */
  public String getLaunchActivityUrl() {
    return launchActivityUrl;
  }

  /**
   * Sets the launch activity URL.
   *
   * @param launchActivityUrl the launch activity URL
   */
  public void setLaunchActivityUrl(String launchActivityUrl) {
    this.launchActivityUrl = launchActivityUrl;
  }

  /**
   * Gets the activity type URI from the first TinCan activity, if present.
   *
   * @return the activity type URI, or {@code null} if absent
   */
  public String getActivityType() {
    return activityType;
  }

  /**
   * Sets the activity type URI.
   *
   * @param activityType the activity type URI
   */
  public void setActivityType(String activityType) {
    this.activityType = activityType;
  }

  /**
   * Gets first activity names keyed by language tag.
   *
   * @return an unmodifiable language map of activity names
   */
  public Map<String, String> getLocalizedNames() {
    return Map.copyOf(localizedNames);
  }

  /**
   * Sets first activity names keyed by language tag.
   *
   * @param localizedNames language map of activity names
   */
  public void setLocalizedNames(Map<String, String> localizedNames) {
    this.localizedNames.clear();
    if (localizedNames != null) {
      this.localizedNames.putAll(localizedNames);
    }
  }

  /**
   * Gets first activity descriptions keyed by language tag.
   *
   * @return an unmodifiable language map of activity descriptions
   */
  public Map<String, String> getLocalizedDescriptions() {
    return Map.copyOf(localizedDescriptions);
  }

  /**
   * Sets first activity descriptions keyed by language tag.
   *
   * @param localizedDescriptions language map of activity descriptions
   */
  public void setLocalizedDescriptions(Map<String, String> localizedDescriptions) {
    this.localizedDescriptions.clear();
    if (localizedDescriptions != null) {
      this.localizedDescriptions.putAll(localizedDescriptions);
    }
  }

  @Override
  public String getManifestFile() {
    return "tincan.xml";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof XapiMetadata that)) {
      return false;
    }

    return new EqualsBuilder()
        .appendSuper(super.equals(o))
        .append(getLaunchActivityId(), that.getLaunchActivityId())
        .append(getLaunchActivityUrl(), that.getLaunchActivityUrl())
        .append(getActivityType(), that.getActivityType())
        .append(getLocalizedNames(), that.getLocalizedNames())
        .append(getLocalizedDescriptions(), that.getLocalizedDescriptions())
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .appendSuper(super.hashCode())
        .append(getLaunchActivityId())
        .append(getLaunchActivityUrl())
        .append(getActivityType())
        .append(getLocalizedNames())
        .append(getLocalizedDescriptions())
        .toHashCode();
  }
}
