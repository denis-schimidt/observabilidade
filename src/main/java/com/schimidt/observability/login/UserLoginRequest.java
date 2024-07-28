package com.schimidt.observability.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;

record UserLoginRequest(@JsonProperty @Email String email, @JsonProperty String rawPassword) {
}
