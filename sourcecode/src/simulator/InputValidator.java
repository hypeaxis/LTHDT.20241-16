package simulator;

public class InputValidator {

    // Kiểm tra nếu giá trị là một số hợp lệ và nằm trong phạm vi cho phép
    public boolean isValid(float value) {
        // Ví dụ kiểm tra xem giá trị có lớn hơn 0 và nhỏ hơn hoặc bằng 300
        return value > 0 && value <= 300;
    }
}
