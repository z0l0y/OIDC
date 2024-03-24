package com.hust.validator;

import com.hust.dto.UserDTO;
import com.hust.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static com.hust.utils.Constants.INVALID_DATA_ERROR_MESSAGE;

public class UserValidator {
    private Validator validator;
    @Autowired
    public void UserService(Validator validator) {
        this.validator = validator;
    }
    public Result UserInfoValidator(UserDTO userDTO) {
        Set<ConstraintViolation<UserDTO>> violations = validator.validate(userDTO);
        if (!violations.isEmpty()) {
            for (ConstraintViolation<UserDTO> violation : violations) {
                System.out.println(violation.getMessage());
            }
            return Result.error(INVALID_DATA_ERROR_MESSAGE);
        }
        return Result.success();
    }
}
