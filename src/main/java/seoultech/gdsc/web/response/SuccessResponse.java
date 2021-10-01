package seoultech.gdsc.web.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessResponse<T> extends BasicResponse{
    private boolean success = true;
    private T data;

    public SuccessResponse(T data) {
        this.data = data;
    }
}
