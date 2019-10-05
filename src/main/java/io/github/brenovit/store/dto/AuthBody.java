package io.github.brenovit.store.dto;

import lombok.Data;

@Data
public class AuthBody {
	private String email;
	private String password;
}
