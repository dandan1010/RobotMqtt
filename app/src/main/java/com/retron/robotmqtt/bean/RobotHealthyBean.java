package com.retron.robotmqtt.bean;

public class RobotHealthyBean {

    /**
     * robot_healthy : {antiPressureFoot:true,cameraNotTrigger:true,cannotRotate:true,deviceTopic:true,driverErrorLeftDriverCommandError:true,driverErrorLeftDriverCommunicationError:true,driverErrorLeftDriverCurrentExceedsLimit:true,driverErrorLeftDriverEmergencyStop:true,driverErrorLeftDriverError:true,driverErrorLeftDriverExternalStop:true,driverErrorLeftDriverHoarePhaseSequenceError:true,driverErrorLeftDriverInitializationError:true,driverErrorLeftDriverMosError:true,driverErrorLeftDriverOverheat:true,driverErrorLeftDriverOverload:true,driverErrorLeftDriverParameterError:true,driverErrorLeftDriverShortCircuit:true,driverErrorLeftDriverVoltageIsBelowFunctioningThreshold:true,driverErrorLeftEEPDataError:true,driverErrorLeftEncoderError:true,driverErrorLeftMotorDriverVoltageExceedsLimit:true,driverErrorLeftMotorExceedsSpeedLimit:true,driverErrorLeftMotorFeedbackError:true,driverErrorLeftMotorIsDisconnected:true,driverErrorLeftMotorOverheat:true,driverErrorLeftMotorPowerOnProtection:true,driverErrorRightDriverCommandError:true,driverErrorRightDriverCommunicationError:true,driverErrorRightDriverCurrentExceedsLimit:true,driverErrorRightDriverEEPDataError:true,driverErrorRightDriverEmergencyStop:true,driverErrorRightDriverError:true,driverErrorRightDriverExternalStop:true,driverErrorRightDriverHoarePhaseSequenceError:true,driverErrorRightDriverInitializationError:true,driverErrorRightDriverMosError:true,driverErrorRightDriverOverheat:true,driverErrorRightDriverOverload:true,driverErrorRightDriverParameterError:true,driverErrorRightDriverShortCircuit:true,driverErrorRightDriverVoltageExceedsLimit:true,driverErrorRightDriverVoltageIsBelowFunctioningThreshold:true,driverErrorRightEncoderSignalError:true,driverErrorRightMotorExceedsSpeedLimit:true,driverErrorRightMotorFeedbackError:true,driverErrorRightMotorIsDisconnected:true,driverErrorRightMotorOverheat:true,driverErrorRightMotorPowerOnProtection:true,healthTopic:true,imuBoard:true,imuTopic:true,laserNotTrigger:true,laserParam:true,laserTopic:true,leftMotor:true,localizationLost:true,odomTopic:true,powerBoard:true,protectorNotTrigger:true,protectorTopic:true,rightMotor:true,robotSurround:true,stuckVirtualWall:true,ultrasonic0:true,ultrasonic1:true,ultrasonic2:true,ultrasonic3:true,ultrasonic4:true,ultrasonic5:true,ultrasonic6:true,ultrasonic7:true,ultrasonicBoard:true,usbSecurity:true}
     * type : robot_healthy
     */
    private String robot_healthy;
    private String type;

    public void setRobot_healthy(String robot_healthy) {
        this.robot_healthy = robot_healthy;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRobot_healthy() {
        return robot_healthy;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "RobotHealthyBean{" +
                "robot_healthy='" + robot_healthy + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
