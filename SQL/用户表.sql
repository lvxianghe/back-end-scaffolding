CREATE TABLE User (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
                      name          VARCHAR(50)  NOT NULL COMMENT '名字',
                      gender        VARCHAR(10)  COMMENT '性别',
                      idCard        VARCHAR(20)  COMMENT '证件号码',
                      phoneNumber   VARCHAR(20)  COMMENT '电话号码',
                      mobileNumber  VARCHAR(20)  COMMENT '手机号码',
                      address       VARCHAR(255) COMMENT '地址',
                      nationality   VARCHAR(100) COMMENT '国籍',
                      country       VARCHAR(100) COMMENT '国家',
                      state         VARCHAR(100) COMMENT '省份',
                      city          VARCHAR(100) COMMENT '市',
                      bankCard      VARCHAR(20)  COMMENT '银行卡',
                      occupation    VARCHAR(100) COMMENT '职业',
                      companyName   VARCHAR(100) COMMENT '公司名称',
                      maritalStatus VARCHAR(20)  COMMENT '婚姻状况',
                      userType      VARCHAR(20)  COMMENT '用户类型:1-内部用户 2-对私客户 3-对公客户',
                      createTime    VARCHAR(20)  COMMENT '创建时间', -- 使用 VARCHAR 存储时间
                      updateTime    VARCHAR(20)  COMMENT '更新时间', -- 使用 VARCHAR 存储时间
                      status        VARCHAR(10)  COMMENT '状态:0-失效 1-有效'
) COMMENT='用户表';