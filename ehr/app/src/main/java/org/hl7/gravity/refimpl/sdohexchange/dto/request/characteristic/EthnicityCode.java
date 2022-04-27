package org.hl7.gravity.refimpl.sdohexchange.dto.request.characteristic;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hl7.gravity.refimpl.sdohexchange.exception.UnknownCodeException;

import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public enum EthnicityCode {

  HISPANIC_OR_LATINO("2135-2", "Hispanic or Latino"),
  NON_HISPANIC_OR_LATINO("2186-5", "Non Hispanic or Latino");

  private final String code;
  private final String display;

  public static final String SYSTEM = "urn:oid:2.16.840.1.113883.6.238";

  @JsonCreator
  public static EthnicityCode fromText(String value) {
    return Stream.of(EthnicityCode.values())
        .filter(targetEnum -> targetEnum.display.equals(value))
        .findFirst()
        .orElseThrow(() -> new UnknownCodeException(String.format("Unsupported Ethnicity code '%s'", value)));
  }
}
