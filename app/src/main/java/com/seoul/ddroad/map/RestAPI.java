package com.seoul.ddroad.map;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class RestAPI {
    // 네이버 API 키값
    String clientId = "5diZho3PLVF6IvUkDN0a";
    String clientSecret = "Wie3JCCLC5";

    URL url;
    String apiURL;
    String title;
    String inputLine;
    int cnt = 0;
    APIData apiData;

    public APIData getinfo(APIData apiData, String type) {
        StringBuffer response = new StringBuffer();
        this.apiData = apiData;

        try {


            // 영화 제목을 UTF-8형식으로 인코딩
            String text = "";
            if (type.equals("cafe")) {
                text = URLEncoder.encode("서울애견카페", "UTF-8");
            } else if (type.equals("hospital")) {
                text = URLEncoder.encode("동물병원", "UTF-8");
            } else if (type.equals("salon")) {
                text = URLEncoder.encode("애견미용실", "UTF-8");
            } else if (type.equals("trail")) {
                text = URLEncoder.encode("서울공원", "UTF-8");
            } else if (type.equals("hotel")) {
                text = URLEncoder.encode("애견호텔", "UTF-8");
            }
            for (int i = 0; i <= 10; i++) {
                int start = 100 * i;

                // 오픈API를 이용하기 위한 URL
                apiURL = "https://openapi.naver.com/v1/search/local?query=" + text + "&display=100&start=" + start;
                url = new URL(apiURL);

                // URL연결
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                // 정보를 얻어올 수 있는 GET메소드
                con.setRequestMethod("GET");

                // 연결을 위한 키 아이디와 비밀번호
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

                int responseCode = con.getResponseCode();
                BufferedReader br;

                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }

                // 원하는 정보를 모두 받아온다. 하지만 가장 최신의 하나것만 사용하기 때문에 break;를 사용
                while ((inputLine = br.readLine()) != null) {
                    if (inputLine.contains("title")) {
                        String arr[] = new String[5];
                        arr = inputLine.split("\"", 5);
//                    response.append(arr[3] + "\n");
                        setTitle(arr[3], type);
                    } else if (inputLine.contains("address")) {
                        String arr[] = new String[5];
                        arr = inputLine.split("\"", 5);
//                    response.append(arr[3] + "\n");
                        setAddress(arr[3], type);

                    } else if (inputLine.contains("roadAddress")) {
                        String arr[] = new String[5];
                        arr = inputLine.split("\"", 5);
//                    response.append(arr[3] + "\n");
                        setRoadAddress(arr[3], type);
                        cnt++;
                    }
                }

                APIData.cafeTotal = cnt - 1;
                br.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return this.apiData;
    }

    public void setTitle(String arr, String type) {
        if (type.equals("cafe")) {
            this.apiData.cafe[cnt].setTitle(arr);
        } else if (type.equals("hospital")) {
            this.apiData.hospital[cnt].setTitle(arr);
        } else if (type.equals("salon")) {
            this.apiData.salon[cnt].setTitle(arr);
        } else if (type.equals("trail")) {
            this.apiData.trail[cnt].setTitle(arr);
        } else if (type.equals("hotel")) {
            this.apiData.hotel[cnt].setTitle(arr);
        }
    }

    public void setAddress(String arr, String type) {
        if (type.equals("cafe")) {
            this.apiData.cafe[cnt].setAddress(arr);
        } else if (type.equals("hospital")) {
            this.apiData.hospital[cnt].setAddress(arr);
        } else if (type.equals("salon")) {
            this.apiData.salon[cnt].setAddress(arr);
        } else if (type.equals("trail")) {
            this.apiData.trail[cnt].setAddress(arr);
        } else if (type.equals("hotel")) {
            this.apiData.hotel[cnt].setAddress(arr);
        }
    }

    public void setRoadAddress(String arr, String type) {
        if (type.equals("cafe")) {
            this.apiData.cafe[cnt].setRoadAddress(arr);
        } else if (type.equals("hospital")) {
            this.apiData.hospital[cnt].setRoadAddress(arr);
        } else if (type.equals("salon")) {
            this.apiData.salon[cnt].setRoadAddress(arr);
        } else if (type.equals("trail")) {
            this.apiData.trail[cnt].setRoadAddress(arr);
        } else if (type.equals("hotel")) {
            this.apiData.hotel[cnt].setRoadAddress(arr);
        }
    }
}