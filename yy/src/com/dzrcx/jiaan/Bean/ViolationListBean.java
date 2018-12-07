package com.dzrcx.jiaan.Bean;

import java.util.List;

/**
 * 事故违章Bean
 */
public class ViolationListBean extends YYBaseResBean {
    private BreakIllegaRecontent returnContent;

    public BreakIllegaRecontent getReturnContent() {
        return returnContent;
    }

    public void setReturnContent(BreakIllegaRecontent returnContent) {
        this.returnContent = returnContent;
    }

    public class BreakIllegaRecontent {
        private int violationCount;
        private int undoViolationCount;
        private int violationPoints;
        private int violationAmount;
        private List<BreakIllegals> violationInfoVoForLists;

        public int getViolationCount() {
            return violationCount;
        }

        public void setViolationCount(int violationCount) {
            this.violationCount = violationCount;
        }

        public int getUndoViolationCount() {
            return undoViolationCount;
        }

        public void setUndoViolationCount(int undoViolationCount) {
            this.undoViolationCount = undoViolationCount;
        }

        public int getViolationPoints() {
            return violationPoints;
        }

        public void setViolationPoints(int violationPoints) {
            this.violationPoints = violationPoints;
        }

        public int getViolationAmount() {
            return violationAmount;
        }

        public void setViolationAmount(int violationAmount) {
            this.violationAmount = violationAmount;
        }

        public List<BreakIllegals> getViolationInfoVoForLists() {
            return violationInfoVoForLists;
        }

        public void setViolationInfoVoForLists(List<BreakIllegals> violationInfoVoForLists) {
            this.violationInfoVoForLists = violationInfoVoForLists;
        }

        public class BreakIllegals {
            private int confirmState;// 确认状态  1 是表示核算完成， 0是表示正在核算
            private int deductCashState; //1已经支付
            private int id;
            private int vType;
            private int orderId;
            private int points;
            private int dealState;
            private int amount;
            private String type;
            private String address;
            private String carBrand;
            private String carSeries;
            private String carLicense;
            private String desc;
            private String nature;
            private long vioTime;
            private double repairCost;
            private double deductCashAmount;
            private double outageLoss;

            public int getConfirmState() {
                return confirmState;
            }

            public void setConfirmState(int confirmState) {
                this.confirmState = confirmState;
            }

            public int getDeductCashState() {
                return deductCashState;
            }

            public void setDeductCashState(int deductCashState) {
                this.deductCashState = deductCashState;
            }

            public String getDesc() {
                return desc;
            }

            public void setDesc(String desc) {
                this.desc = desc;
            }

            public double getRepairCost() {
                return repairCost;
            }

            public void setRepairCost(double repairCost) {
                this.repairCost = repairCost;
            }

            public double getDeductCashAmount() {
                return deductCashAmount;
            }

            public void setDeductCashAmount(double deductCashAmount) {
                this.deductCashAmount = deductCashAmount;
            }

            public double getOutageLoss() {
                return outageLoss;
            }

            public void setOutageLoss(double outageLoss) {
                this.outageLoss = outageLoss;
            }

            public String getCarSeries() {
                return carSeries;
            }

            public void setCarSeries(String carSeries) {
                this.carSeries = carSeries;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public int getvType() {
                return vType;
            }

            public void setvType(int vType) {
                this.vType = vType;
            }

            public int getOrderId() {
                return orderId;
            }

            public void setOrderId(int orderId) {
                this.orderId = orderId;
            }

            public int getPoints() {
                return points;
            }

            public void setPoints(int points) {
                this.points = points;
            }

            public int getDealState() {
                return dealState;
            }

            public void setDealState(int dealState) {
                this.dealState = dealState;
            }

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getCarBrand() {
                return carBrand;
            }

            public void setCarBrand(String carBrand) {
                this.carBrand = carBrand;
            }

            public String getCarLicense() {
                return carLicense;
            }

            public void setCarLicense(String carLicense) {
                this.carLicense = carLicense;
            }

            public String getNature() {
                return nature;
            }

            public void setNature(String nature) {
                this.nature = nature;
            }

            public long getVioTime() {
                return vioTime;
            }

            public void setVioTime(long vioTime) {
                this.vioTime = vioTime;
            }
        }
    }
}
