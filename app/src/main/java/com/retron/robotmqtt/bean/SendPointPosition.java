package com.retron.robotmqtt.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class SendPointPosition implements Serializable {
    /*"sendPointPosition":[{"point_Name":"Origin","point_x":255,"point_y":671,"angle":0,"point_type":0},{"point_Name":"charging","point_x":237,"point_y":673,"angle":0.3209468264235981,"point_type":1},{"point_Name":"Initialize","point_x":240,"point_y":673,"angle":0.5296567770275211,"point_type":0},{"point_Name":"接待室","point_x":350,"point_y":709,"angle":-3.2440617173289197,"point_type":2},{"point_Name":"彩超二室","point_x":560,"point_y":711,"angle":0.4980016075110114,"point_type":2},{"point_Name":"口腔科","point_x":774,"point_y":713,"angle":1.2128548934987826,"point_type":2},{"point_Name":"Dr摄影片室","point_x":960,"point_y":654,"angle":-89.3012684104432,"point_type":2},{"point_Name":"洗手间","point_x":963,"point_y":457,"angle":-89.38223399473583,"point_type":2},{"point_Name":"电离室","point_x":718,"point_y":559,"angle":179.37189865504968,"point_type":2},{"point_Name":"体检采血处","point_x":188,"point_y":599,"angle":-178.8904920861161,"point_type":2},{"point_Name":"妇科","point_x":1076,"point_y":775,"angle":-2.9039335415821377,"point_type":2},{"point_Name":"内科","point_x":954,"point_y":769,"angle":94.57784390094317,"point_type":2},{"point_Name":"耳鼻喉科","point_x":779,"point_y":780,"angle":86.64316131714362,"point_type":2},{"point_Name":"心电图二","point_x":664,"point_y":750,"angle":81.05498026835305,"point_type":2},{"point_Name":"心电图一","point_x":612,"point_y":753,"angle":64.55794796099649,"point_type":2},{"point_Name":"彩超二","point_x":552,"point_y":746,"angle":87.22210627100083,"point_type":2},{"point_Name":"彩超一","point_x":487,"point_y":769,"angle":68.93039780007672,"point_type":2},{"point_Name":"血压室","point_x":407,"point_y":767,"angle":53.83000166037752,"point_type":2},{"point_Name":"眼科","point_x":460,"point_y":617,"angle":89.714799843426,"point_type":2},{"point_Name":"视力科","point_x":507,"point_y":624,"angle":69.85053307864312,"point_type":2},{"point_Name":"动脉硬化检查","point_x":556,"point_y":592,"angle":49.85599876919411,"point_type":2},{"point_Name":"骨密度","point_x":621,"point_y":624,"angle":78.06975663664589,"point_type":2},{"point_Name":"体检登记","point_x":282,"point_y":632,"angle":87.8327458035872,"point_type":2},{"point_Name":"End","point_x":498,"point_y":571,"angle":-172.89815877650037,"point_type":0},{"point_Name":"Current","point_x":237,"point_y":673,"angle":0.6534564205494079,"point_type":0}]}*/
    private String type;
    private String name;
    private ArrayList<PointDataBean> sendPointPosition;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<PointDataBean> getSendPointPosition() {
        return sendPointPosition;
    }

    public void setSendPointPosition(ArrayList<PointDataBean> sendPointPosition) {
        this.sendPointPosition = sendPointPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
