package com.cyber.restory.utils

import com.cyber.restory.presentation.custom.Region

class MapUtils {

    companion object {
        /*
        * 각 지역별 도청 좌표 구하기
        * */
        fun getCoordinate(region: Region): Pair<Double, Double> {
            return when (region.code) {
                // 서울
                "SEOUL" -> 37.56677014292466 to 126.97865227425055
                // 경기
                "GYEONGGI" -> 37.26321417323435 to 127.03286213616008
                // 강원
                "GANGWON" -> 37.8853257858209 to 127.729829010354
                // 부산
                "BUSAN" -> 35.179675369619964 to 129.0750113723
                // 인천
                "INCHEON" -> 37.455923629216066 to 126.70536010784367
                // 대구
                "DAEGU" -> 35.8713802646197 to 128.601805491072
                // 대전
                "DAEJEON" -> 36.3505388992836 to 127.38483484675
                // 광주
                "GWANGJU" -> 35.1601037626662 to 126.851629955742
                // 울산
                "ULSAN" -> 35.5395955247058 to 129.311603446508
                // 충북
                "CHUNGBUK" -> 36.63527014888193 to 127.49183021883579
                // 충남
                "CHUNGNAM" -> 36.6588292532864 to 126.672776193822
                // 경남
                "GYEONGNAM" -> 35.2377742104522 to 128.69189688916
                // 경북
                "GYEONGBUK" -> 36.5759962255808 to 128.505799255401
                // 제주
                "JEJU" -> 33.4889179032603 to 126.498229141199
                // 전남
                "JEONNAM" -> 34.816111078663184 to 126.4628078191417
                // 전북
                "JEONBUK" -> 35.8201963639272 to 127.108976712011
                // 그 외 서울
                else -> 37.56677014292466 to 126.97865227425055
            }
        }

    }
}