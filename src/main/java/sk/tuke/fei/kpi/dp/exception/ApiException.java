package sk.tuke.fei.kpi.dp.exception;

public class ApiException extends RuntimeException {

  private final String errorMessage;
  private final FaultType faultType;

  public ApiException(FaultType faultType, String errorMsg) {
    super(errorMsg);
    this.faultType = faultType;
    this.errorMessage = errorMsg;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public FaultType getFaultType() {
    return faultType;
  }

}
