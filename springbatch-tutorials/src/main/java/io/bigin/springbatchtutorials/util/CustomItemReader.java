
/*
 * Created by Mingi Park on 2021/10/19
 * As part of Bigin
 *
 * Copyright (C) Bigin (https://bigin.io/main) - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Dev Backend Team <mingi@bigin.io>, 2021/10/19
 */

package io.bigin.springbatchtutorials.util;

import java.util.Collections;
import java.util.List;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

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
public class CustomItemReader<T> implements ItemReader<T> {
  private final List<T> items;

  public CustomItemReader(List<T> items) {
    this.items = items;
  }

  @Override
  public T read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    if(!items.isEmpty()) {
      return items.remove(0);
    }
    return null;
  }
}
