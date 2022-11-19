package com.example.app.dto.lot;


import com.example.app.models.LotStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LotPageFindAuthDto  implements Serializable {

    private Long id;

    private String name;

    private String photo;

    private Long price;


}
