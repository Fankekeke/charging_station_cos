package cc.mrbird.febs.cos.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 充电桩状态
 *
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class SpaceStatusInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 充电桩ID
     */
    private Integer spaceId;

    /**
     * 状态（-1.预约中 0.空闲 1.充电中）
     */
    private String status;

    @TableField(exist = false)
    private String spaceName;

    @TableField(exist = false)
    private String code;

    @TableField(exist = false)
    private Integer pharmacyId;
}
