package org.xiaoxingbomei.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Java stream流学习
 */
@Slf4j
@Service
public class Java_Stream
{
    /**
     * 创建Stream的多种方式
     */
    @Test
    public void createStream()
    {
        log.info("1.创建stream的多种方式");

        // 从集合创建
        List<String> list = Arrays.asList("java", "redis", "mysql", "oracle", "sqlserver");
        Stream<String> streamFromList = list.stream(); // 转换为stream流
        log.info("从List创建Stream:{}", streamFromList.collect(Collectors.toList())); // 再转回List

        // 从数组创建
        String[] array = {"java","redis","mysql","oracle","sqlserver"};
        Stream<String> streamFromArray = Arrays.stream(array);
        log.info("从Array创建Stream:{}", streamFromArray.collect(Collectors.toList()));

        // 使用Stream.of()创建
        Stream<String> streamFromOf = Stream.of("java", "redis", "mysql", "oracle", "sqlserver");
        log.info("从Array创建Stream:{}", streamFromOf.collect(Collectors.toList()));

        // 创建空Stream
        Stream<String> emptyStream = Stream.empty();
        log.info("创建空的Stream:{}",emptyStream.collect(Collectors.toList()));

        // 创建无限Stream,使用limit限制大小
        Stream<Integer> infiniteStream = Stream.iterate(0, n -> n + 2).limit(10);
        log.info("创建无限流（偶数）Stream:{}",infiniteStream.collect(Collectors.toList()));

        // 使用 Stream.generate()
        Stream<Double> streamFromGenerate = Stream.generate(Math::random).limit(10);
        log.info("创建无限流（随机数）Stream:{}",streamFromGenerate.collect(Collectors.toList()));

        // 基本类型stream（避免装箱的性能开销）
        IntStream intStream = IntStream.range(1, 6);
        log.info("基本类型stream（避免装箱拆箱的性能开销）{}",intStream.boxed().collect(Collectors.toList()));


    }


}
