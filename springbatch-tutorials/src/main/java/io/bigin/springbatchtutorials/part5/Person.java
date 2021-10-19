
/*
 * Created by Mingi Park on 2021/10/19
 * As part of Bigin
 *
 * Copyright (C) Bigin (https://bigin.io/main) - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Dev Backend Team <mingi@bigin.io>, 2021/10/19
 */

package io.bigin.springbatchtutorials.part5;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * create on 2021/10/19. create by IntelliJ IDEA.
 *
 * <p> 클래스 설명 </p>
 * <p> {@link } and {@link }관련 클래스 </p>
 *
 * @author Mingi Park
 * @version 1.0
 * @see
 * @since 지원하는 자바버전 (ex : 5+ 5이상)
 */
@Entity(name = "person")
@NoArgsConstructor(access = AccessLevel.PROTECTED) //JPA const
@Getter
@ToString
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String name;

  private int age;

  private String address;

  public Person(long id, String name, int age, String address) {
    this.id = id;
    this.name = name;
    this.age = age;
    this.address = address;
  }

  public Person(String name, int age, String address) {
    this.name = name;
    this.age = age;
    this.address = address;
  }
}
