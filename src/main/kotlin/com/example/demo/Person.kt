package com.example.demo

import jakarta.persistence.*

@Entity // https://www.baeldung.com/kotlin/jpa
data class Person(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	val id: Int? = null,
	@Column(nullable = false)
	val name: String)


