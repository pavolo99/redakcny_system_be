package sk.tuke.fei.kpi.dp.dto.error;

import sk.tuke.fei.kpi.dp.exception.FaultType;

public class ErrorDto {

  private int errorCode;
  private FaultType faultType;
  private String message;

  public ErrorDto(int errorCode, String message, FaultType faultType) {
    this.errorCode = errorCode;
    this.message = message;
    this.faultType = faultType;
  }

  public ErrorDto(String message, FaultType faultType) {
    this.message = message;
    this.faultType = faultType;
  }

  public int getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(int errorCode) {
    this.errorCode = errorCode;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public FaultType getFaultType() {
    return faultType;
  }

  public void setFaultType(FaultType faultType) {
    this.faultType = faultType;
  }
}
