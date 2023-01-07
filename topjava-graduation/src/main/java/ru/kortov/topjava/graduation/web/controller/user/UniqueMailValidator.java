package ru.kortov.topjava.graduation.web.controller.user;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import ru.kortov.topjava.graduation.HasIdAndEmail;
import ru.kortov.topjava.graduation.repository.UserRepository;
import ru.kortov.topjava.graduation.web.SecurityUtil;

@Component
@AllArgsConstructor
public class UniqueMailValidator implements org.springframework.validation.Validator {
    public static final String EXCEPTION_DUPLICATE_EMAIL = "User with this email already exists";

    private final UserRepository repository;
    private final HttpServletRequest request;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return HasIdAndEmail.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        HasIdAndEmail user = ((HasIdAndEmail) target);
        if (StringUtils.hasText(user.getEmail())) {
            repository.findByEmailIgnoreCase(user.getEmail())
                      .ifPresent(dbUser -> {
                          if (request.getMethod().equals("PUT")) {  // UPDATE
                              int dbId = dbUser.id();

                              // it is ok, if update ourselves
                              if (user.getId() != null && dbId == user.id()) return;

                              // Workaround for update with user.id=null in request body
                              // ValidationUtil.assureIdConsistent called after this validation
                              String requestURI = request.getRequestURI();
                              if (requestURI.endsWith("/" + dbId) ||
                                  (dbId == SecurityUtil.authId() && requestURI.contains("/profile"))) {
                                  return;
                              }
                          }
                          errors.rejectValue("email", "", EXCEPTION_DUPLICATE_EMAIL);
                      });
        }
    }
}
