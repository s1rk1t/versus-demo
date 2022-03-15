package com.versus.versus01.pojo;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Component
@AllArgsConstructor
@NoArgsConstructor
public class HomeGames {

	private int games;

	private int wins;

	private int losses;

	private int ties;

}
