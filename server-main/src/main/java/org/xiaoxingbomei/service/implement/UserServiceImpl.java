package org.xiaoxingbomei.service.implement;

import cn.idev.excel.EasyExcel;
import cn.idev.excel.write.metadata.style.WriteCellStyle;
import cn.idev.excel.write.style.HorizontalCellStyleStrategy;
import cn.idev.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.hankcs.hanlp.HanLP;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.security.AuthenticationAuditListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.xiaoxingbomei.common.entity.GlobalEntity;
import org.xiaoxingbomei.config.Thread.DynamicThreadPool;
import org.xiaoxingbomei.dao.localhost.UserMapper;
import org.xiaoxingbomei.service.UserService;
import org.xiaoxingbomei.utils.Exception_Utils;
import org.xiaoxingbomei.vo.User;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService
{

    @Autowired
    UserMapper userMapper;

    @Autowired
    DynamicThreadPool dynamicThreadPool;

    private String localDictionaryPath = "D:\\Environment\\elasticsearch-7.10.2\\plugins\\ik\\config\\custom\\mydict.dic";


    // =========================================================================


    public UserServiceImpl() throws IOException
    {
        // 初始化时加载词典内容到缓存
        loadDictionaryCache();
    }


    @Override
    public GlobalEntity getAllUserInfo()
    {
        // 从数据库中获取全部用户信息
        List<User> allUserInfo = userMapper.getAllUserInfo();

        // 构建结果返参
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("allUserInfo", allUserInfo);
        return GlobalEntity.success(resultMap,"获取全部用户信息");
    }

    @Override
    public void exportUserInfoTemplate(HttpServletResponse response) throws Exception
    {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("UserInfoTemplate", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        // 模拟数据（可以为空，仅生成表头）
        List<User> users = new ArrayList<>();

        // 设置表头样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex()); // 背景颜色
//        headWriteCellStyle.setFont(new Font(16, true)); // 字体大小和加粗

        // 设置内容样式
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();

        // 注册样式策略
        HorizontalCellStyleStrategy styleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        // 生成 Excel 文件并写入响应流
        EasyExcel.write(response.getOutputStream(), User.class)
                .registerWriteHandler(styleStrategy) // 应用样式
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy()) // 自动调整列宽
                .sheet("用户信息")
                .doWrite(users);
    }

    @Override
    public List<String> discoverNewWords(List<String> texts)
    {
        // 使用 dynamicThreadPool 提交每个文本的关键词提取任务
        List<Future<List<String>>> futures = texts.stream()
                // 对每个文本进行 HanLP 关键词提取，返回一个 Future 对象
                .map(text -> dynamicThreadPool.submit(() -> HanLP.extractKeyword(text, 10)))
                .collect(Collectors.toList()); // 将所有 Future 对象收集到一个列表中

        // 对所有 Future 结果进行处理
        return futures.stream()
                // 对每个 Future 对象进行处理，尝试获取其计算结果
                .flatMap(future ->
                {
                    try
                    {
                        // 获取结果并将其转换为流，再进行合并
                        return future.get().stream();
                    } catch (Exception e)
                    {
                        // 如果获取结果时发生异常，则抛出运行时异常
                        throw new RuntimeException(e);
                    }
                })
                // 使用 distinct 去重，确保返回的词语列表没有重复项
                .distinct()
                // 收集最终的去重词语列表
                .collect(Collectors.toList());
    }


    // 内存中的词典缓存
    private Set<String> dictionaryCache = new HashSet<>();

    // 加载词典内容到缓存
    private void loadDictionaryCache() throws IOException
    {
        log.info("apollo.elasticsearch.local.dictionary.path:{}", localDictionaryPath);
        Path path = Paths.get(localDictionaryPath);
        if (Files.exists(path)) {
            dictionaryCache = new HashSet<>(Files.readAllLines(path));
        }
    }

    @Override
    public void updateLocalDictionary(List<String> newWords)
    {
        log.info("开始更新词典，新词数量：{}", newWords.size());

        // 去重：只添加词典中不存在的新词
        Set<String> uniqueNewWords = new HashSet<>(newWords);
        uniqueNewWords.removeAll(dictionaryCache);

        // 将新词追加到文件
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(localDictionaryPath, true)))
        {
            for (String word : uniqueNewWords)
            {
                writer.write(word);
                writer.newLine();
            }
        }catch (Exception e)
        {
            Exception_Utils.recursiveReversePrintStackCauseCommon(e);
            log.error("词典更新失败：{}", e.getMessage());
        }

        // 更新缓存
        dictionaryCache.addAll(uniqueNewWords);

        log.info("词典更新完成，新增词语数量：{}", uniqueNewWords.size());

    }





}
