package ru.clevertec.ecl.model.entity;

import java.io.Serializable;

public interface BaseEntity<K extends Serializable> {

    K getId();
}
