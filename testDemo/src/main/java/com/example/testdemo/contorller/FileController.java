package com.example.testdemo.contorller;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.example.testdemo.component.Result;
import com.example.testdemo.entity.*;
import com.example.testdemo.entity.Record;
import com.example.testdemo.service.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@RestController
@RequestMapping("/file")
public class FileController {
    private static final String FORMAT = "Item Name,Item Type,Item Price,Retrieval City,Retrieval Start Time,Retrieval Courier,Retrieval Courier Gender,Retrieval Courier Phone Number,Retrieval Courier Age,Delivery Finished Time,Delivery City,Delivery Courier,Delivery Courier Gender,Delivery Courier Phone Number,Delivery Courier Age,Item Export City,Item Export Tax,Item Export Time,Item Import City,Item Import Tax,Item Import Time,Container Code,Container Type,Ship Name,Company Name,Log Time";
    private static final String DELIMITER = ",";

    @Resource
    CompanyService companyService;
    @Resource
    ShipService shipService;
    @Resource
    ContainerService containerService;
    @Resource
    CityService cityService;
    @Resource
    CourierService courierService;
    @Resource
    RecordService recordService;
    @Resource
    HandleService handleService;
    @Resource
    TransitService transitService;

    @CrossOrigin
    @PostMapping("/upload")
    public Result<?> upload(MultipartFile file) throws ParseException {
        String originalFilename = file.getOriginalFilename();
        String flag = IdUtil.fastSimpleUUID();
        String root = System.getProperty("user.dir") + "\\testDemo\\files\\"+flag+"_"+ originalFilename;
        try {
            FileUtil.writeBytes(file.getBytes(),root);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("201",e.getMessage());
        }
        try (BufferedReader br = Files.newBufferedReader(Paths.get(root), Charset.forName("GBK"))) {
            // 按行读取
            String line = br.readLine();
            if (!line.equals(FORMAT)){
                System.out.println("csv 文件格式不正确！");
                return Result.error("103", "csv 文件格式不正确！");
            }
            while ((line = br.readLine()) != null) {
                String[] columns = line.split(DELIMITER);
                final int companyId = companyService.add(columns[24]);
                final int city1Id = cityService.add(new City(columns[3], null));
                final int city2Id = cityService.add(new City(columns[10], null));
                final int cityEx = cityService.add(new City(columns[15], null));
                final int cityIn = cityService.add(new City(columns[18], null));
                final int containerId = containerService.add(new Container(columns[21], columns[22], null));
                final int shipId = shipService.add(new Ship(columns[23], companyId, null));

                Courier courier1 = new Courier(columns[7], columns[5], Integer.parseInt(columns[8]), "男".equals(columns[6]) ? 1 : 2, companyId, city1Id, null);
                Courier courier2 = new Courier(columns[13], columns[11], Integer.parseInt(columns[14]), "男".equals(columns[12]) ? 1 : 2, companyId, city2Id, null);
                final int c1 = courierService.add(courier1);
                final int c2 = courierService.add(courier2);

                final int recordId = recordService.add(new Record(new Timestamp(new SimpleDateFormat("yyyy/MM/dd hh:mm").parse(columns[25]).getTime()),
                        columns[0], columns[1], Long.parseLong(columns[2]), companyId, containerId, shipId, null));
                if (recordId == -1){
                    continue;
                }
                handleService.add(new Handle(1, new SimpleDateFormat("yyyy/MM/dd").parse(columns[4]), recordId, c1, null));
                handleService.add(new Handle(4, new SimpleDateFormat("yyyy/MM/dd").parse(columns[9]), recordId, c2, null));

                transitService.add(new Transit(1, new SimpleDateFormat("yyyy/MM/dd").parse(columns[4]), null, city1Id, recordId, null));
                transitService.add(new Transit(2, new SimpleDateFormat("yyyy/MM/dd").parse(columns[17]), Double.parseDouble(columns[16]), cityEx, recordId, null));
                transitService.add(new Transit(3, new SimpleDateFormat("yyyy/MM/dd").parse(columns[20]), Double.parseDouble(columns[19]), cityIn, recordId, null));
                transitService.add(new Transit(4, new SimpleDateFormat("yyyy/MM/dd").parse(columns[9]), null, city2Id, recordId, null));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return Result.success();
    }

}
