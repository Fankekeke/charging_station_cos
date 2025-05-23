package cc.mrbird.febs.cos.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 订单评价
 *
 * @author FanK
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class EvaluateInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 所属订单
     */
    private Integer orderId;

    /**
     * 评价用户
     */
    private Integer userId;

    /**
     * 评价内容
     */
    private String content;

    /**
     * 评价分数
     */
    private BigDecimal score;

    @TableField(exist = false)
    private BigDecimal overScore;

    /**
     * 评价时间
     */
    private String createDate;

    /**
     * 评价图片
     */
    private String images;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String orderCode;

    @TableField(exist = false)
    private String merchantName;

    @TableField(exist = false)
    private Integer staffId;

    @TableField(exist = false)
    private Integer pharmacyId;
}
