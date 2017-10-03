package com.pchudzik.blog.example.domainevents;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
class DescriptionUpdated {
	final Long entityId;
	final String oldDescription;
	final String newDescription;
}
