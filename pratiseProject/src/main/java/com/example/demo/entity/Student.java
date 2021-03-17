package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@TableName("student")
public class Student {

    @TableId(value = "ID", type = IdType.UUID)
    @Length( max = 32 )
    @ApiModelProperty("ID")
    private String id;

    @TableField("name")
    @Length( max = 32 )
    @ApiModelProperty("name")
    private String name;

    @TableField("sex")
    @Length( max = 2)
    @ApiModelProperty("sex")
    private String sex;

    @TableField("age")
    @Length( max = 4)
    @ApiModelProperty("age")
    private Integer age;


}
