package com.company.pojo;

import java.util.List;
import com.company.Serde;
import com.company.custom.ModelYear;


public class CarSerde implements Serde<Car> {

  @Override
  public int encode(
      MutableDirectBuffer buffer,
       int offset,
      MessageHeaderEncoder headerEncoder,
      Car value) {
    encoder.wrapAndApplyHeader(buffer, offset, headerEncoder);
    encode(value);
    return encodedLength;
  }

  @Override
  public Car decode(
      MutableDirectBuffer buffer, int offset, MessageHeaderDecoder headerDecoder) {
    decoder.wrap(
        buffer,
        headerDecoder.encodedLength() + offset,
        headerDecoder.blockLength(),
        headerDecoder.version());
    return decode();
  }
}