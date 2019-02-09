package com.packt.snake;

import java.util.List;
import java.util.ArrayList;

import static com.packt.snake.MyAssetsManager.V_HEIGHT;
import static com.packt.snake.MyAssetsManager.V_WIDTH;

public class AstarAgent {
    private List<AstarMap> openList;
    private List<AstarMap> closeList;

    public AstarAgent(){
        openList = new ArrayList<AstarMap>();
        closeList = new ArrayList<AstarMap>();
    }

    public List<AstarMap> findPath(AstarMap start, AstarMap end) {
        AstarMap center = null;
        openList.add(start);
        double minF = 0.0;
        boolean Flag = false;
        List<AstarMap> path = new ArrayList<AstarMap>();
        while (!Flag) {
            if (openList.size() == 0) {
                continue;
            } else {
                center = openList.get(0);
                minF = center.getF();
                for (AstarMap iter : openList) {
                    if (iter.getF() < minF) {
                        minF = iter.getF();
                        center = iter;
                    }
                }
            }
            closeList.add(center);
            openList.remove(center);
            if (closeList.contains(end)) {
                break;
            }

            List<AstarMap> aroundDirectList = findDirectAround(center);
            List<AstarMap> aroundDiagoiseList = findDirectAround(center);
            choosePathElement(aroundDirectList, aroundDiagoiseList, center, end);
        }

        do {
            path.add(center);
            if(center.getPrevious()!=null){
                center = center.getPrevious();
            }
        }while(center.getPrevious()!=null);

        return path;
    }


    public List<AstarMap> findDirectAround(AstarMap center) {

        List<AstarMap> aroundDirectList = new ArrayList<AstarMap>();
        int x = center.getX();
        int y = center.getY();
        AstarMap cell;

        if(x + 15 < V_WIDTH / 15) {
            cell = new AstarMap(x+15, y);
            cell.setPrevious(center);
            aroundDirectList.add(cell);
        }

        if(x - 15 >= 0) {
            cell = new AstarMap(x-15, y);
            cell.setPrevious(center);
            aroundDirectList.add(cell);
        }

        if(y - 15 >= 0) {
            cell = new AstarMap(x, y-15);
            cell.setPrevious(center);
            aroundDirectList.add(cell);
        }

        if(y + 15 < V_HEIGHT / 15){
            cell = new AstarMap(x, y+15);
            cell.setPrevious(center);
            aroundDirectList.add(cell);
        }
        return aroundDirectList;
    }

    public List<AstarMap> findDiagnosisAround(AstarMap center) {

        List<AstarMap> aroundDiagnosisList = new ArrayList<AstarMap>();
        int x = center.getX();
        int y = center.getY();
        AstarMap cell;

        if((x - 15 >= 0) && (y - 15 >= 0)){
            cell = new AstarMap(x-15, y-15);
            cell.setPrevious(center);
            aroundDiagnosisList.add(cell);
        }

        if((x - 15 >= 0) && (y + 15 < V_HEIGHT / 15)) {
            cell = new AstarMap(x-15, y+15);
            cell.setPrevious(center);
            aroundDiagnosisList.add(cell);
        }

        if((x + 15 < V_WIDTH / 15) && (y + 15 < V_HEIGHT)){
            cell = new AstarMap(x+15, y+15);
            cell.setPrevious(center);
            aroundDiagnosisList.add(cell);
        }

        if((x + 15 < V_WIDTH / 15) && (y - 15 >= 0)){
            cell = new AstarMap(x+15, y-15);
            cell.setPrevious(center);
            aroundDiagnosisList.add(cell);
        }
        return aroundDiagnosisList;
    }

    public void choosePathElement(List<AstarMap> aroundDirectList, List<AstarMap> aroundDiagnosisList, AstarMap center, AstarMap end){
        for(AstarMap element: aroundDirectList) {
            if(closeList.contains(element)){
                continue;
            }
            if (!openList.contains(element)){
                element.setG(center.getG() + 15);
                element.setH(end);
                element.setF();
                openList.add(element);
            }else {
                if(center.getH() + 15 < center.getH()) {
                    center.setPrevious(center);
                    center.setG(center.getG() + 15);
                }
            }
        }

        for(AstarMap element: aroundDiagnosisList) {
            if(closeList.contains(element)){
                continue;
            }
            if (!openList.contains(element)){
                element.setG(center.getG() + 21.21); //15 times root(2)
                element.setH(end);
                element.setF();
                openList.add(element);
            }else {
                if(center.getH() + 21.21 < center.getH()) {
                    center.setPrevious(center);
                    center.setG(center.getG() + 21.21);
                }
            }
        }

    }

    public void restart(){
        openList.removeAll(openList);
        closeList.removeAll(closeList);
    }



}
