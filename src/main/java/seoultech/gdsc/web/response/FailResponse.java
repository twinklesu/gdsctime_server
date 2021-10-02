package seoultech.gdsc.web.response;

import lombok.Getter;
import lombok.Setter;
import seoultech.gdsc.web.serializer.EmptyJsonResponse;

@Getter
@Setter
public class FailResponse<T> extends BasicResponse{
    private boolean success = false;
    private EmptyJsonResponse data;
    private String message;

    public FailResponse(String message) {
        this.message = message;
        this.data = new EmptyJsonResponse();
    }
}
