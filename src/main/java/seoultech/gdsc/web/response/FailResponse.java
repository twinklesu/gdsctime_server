package seoultech.gdsc.web.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FailResponse<T> extends BasicResponse{
    private boolean success = false;
    private T data;
    private String message;

    public FailResponse(String message) {
        this.message = message;
    }
}
