package org.hl7.gravity.refimpl.sdohexchange.dto.request.characteristic;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hl7.gravity.refimpl.sdohexchange.exception.UnknownCodeException;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum SexualOrientationCode {

  BISEXUAL("LA22877-7", "Bisexual"),
  HETEROSEXUAL("LA22876-9", "Heterosexual"),
  HOMOSEXUAL("LA22875-1", "Homosexual"),
  OTHER("LA46-8", "Other"),
  ASKED_BUT_UNKNOWN("LA20384-6", "Asked but unknown");

  private final String code;
  private final String display;

  public static final String SYSTEM = "http://loinc.org";

  @JsonCreator
  public static SexualOrientationCode fromText(String value) {
    return Stream.of(SexualOrientationCode.values())
        .filter(targetEnum -> targetEnum.display.equals(value))
        .findFirst()
        .orElseThrow(() -> new UnknownCodeException(String.format("Unsupported Sexual Orientation code '%s'", value)));
  }
}
