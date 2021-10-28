package seoultech.gdsc.web.response;

import lombok.Getter;
import lombok.Setter;
import seoultech.gdsc.web.serializer.EmptyJsonResponse;

import java.util.HashMap;

@Getter
@Setter
public class FailResponse<T> extends BasicResponse{
    private boolean success = false;
    private T data;
    private String message;

    public FailResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }
}
