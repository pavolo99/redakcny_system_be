package sk.tuke.fei.kpi.dp.exception;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;
import sk.tuke.fei.kpi.dp.dto.error.ErrorDto;

@Produces
@Singleton
@Requires(classes = {ApiException.class, ExceptionHandler.class})
public class ApiExceptionHandler implements ExceptionHandler<ApiException, HttpResponse<ErrorDto>> {

  @Override
  public HttpResponse<ErrorDto> handle(HttpRequest request, ApiException exception) {
    ErrorDto errorDto = new ErrorDto(exception.getErrorMessage(), exception.getFaultType());
    switch (exception.getFaultType()) {
      case INVALID_PARAMS:
        errorDto.setErrorCode(400);
        return HttpResponse.badRequest(errorDto);
      case FORBIDDEN:
        errorDto.setErrorCode(403);
        return HttpResponse.badRequest(errorDto);
      case UNAUTHORIZED:
        return HttpResponse.unauthorized();
      case RECORD_NOT_FOUND:
        errorDto.setErrorCode(404);
        return HttpResponse.notFound(errorDto);
      default:
        errorDto.setErrorCode(500);
        return HttpResponse.serverError(errorDto);
    }
  }
}
