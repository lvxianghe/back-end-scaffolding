package org.xiaoxingbomei.service.implement;

import cn.idev.excel.EasyExcel;
import cn.idev.excel.write.metadata.style.WriteCellStyle;
import cn.idev.excel.write.style.HorizontalCellStyleStrategy;
import cn.idev.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xiaoxingbomei.common.entity.GlobalEntity;
import org.xiaoxingbomei.dao.localhost.UserMapper;
import org.xiaoxingbomei.service.UserService;
import org.xiaoxingbomei.vo.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class UserServiceImpl implements UserService
{

    @Autowired
    UserMapper userMapper;

    // =========================================================================


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
}
