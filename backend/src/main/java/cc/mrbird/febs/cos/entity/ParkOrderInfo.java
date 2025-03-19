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
 * 充电出入库管理
 *
 * @author Fank gmail - fan1ke2ke@gmail.com
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ParkOrderInfo implements Serializable {

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
     * 车辆ID
     */
    private Integer vehicleId;

    /**
     * 订单编号
     */
    private String code;

    /**
     * 开始充电时间
     */
    private String startDate;

    /**
     * 充电结束时间
     */
    private String endDate;

    /**
     * 充电时常（分钟）
     */
    private BigDecimal totalTime;

    /**
     * 充电桩单价
     */
    private BigDecimal price;

    /**
     * 总价格
     */
    private BigDecimal totalPrice;

    /**
     * 支付时间
     */
    private String payDate;

    /**
     * 支付状态（0.未支付 1.已支付）
     */
    private String status;

    /**
     * 备注
     */
    private String content;

    @TableField(exist = false)
    private String spaceName;

    @TableField(exist = false)
    private String userName;

    @TableField(exist = false)
    private String vehicleNumber;

    @TableField(exist = false)
    private Integer userId;
}
