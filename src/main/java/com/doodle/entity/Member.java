package com.doodle.entity;



import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Member {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id", nullable = false)
	private Long id;
	
	@Column(name = "email", length = 50)
	private String email;
	
	@Column(name = "password") 
	private String password;
	
	@Column(name = "nickname", length = 50)
	private String nickname;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role role;
	
}
