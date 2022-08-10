package com.kdt.lecture.domain.parent;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Getter
@Setter
@Entity
public class Parent {
    @EmbeddedId
    private ParentId id;
}
