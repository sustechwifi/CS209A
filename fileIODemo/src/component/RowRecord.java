package component;


import java.security.Timestamp;
import java.util.Date;

public class RowRecord {
    private Integer recordId = null;
    private String itemName = "";
    private String itemClass = "";
    private Long itemPrice = null;

    private Date retrievalDate = null;
    private String retrievalCity = "";
    private String retrievalCourier = "";
    private Integer retrievalCourierGender = null;
    private String retrievalCourierPhone = "";
    private Integer retrievalCourierAge = null;


    private Date deliveryDate = null;
    private String deliveryCity = "";
    private String deliveryCourier = "";
    private Integer deliveryCourierGender = null;
    private String deliveryCourierPhone = "";
    private Integer deliveryCourierAge = null;


    private String exportCity = "";
    private Double exportTax = null;
    private Date exportDate = null;

    private String importCity = "";
    private Double importTax = null;
    private Date importDate = null;

    private String containerCode = "";
    private String containerType = "";

    private String ship = "";
    private String company = "";
    private Timestamp logTime = null;

}
