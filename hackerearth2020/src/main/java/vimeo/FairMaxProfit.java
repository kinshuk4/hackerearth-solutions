package vimeo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * @author Kinshuk Chandra
 * @date
 * @link https://www.hackerearth.com/challenges/hiring/vimeo-full-stack-developer-hiring-challenge-2020/problems/d824ac6ec06942f1a8b70a9248d7b852/
 */

class FairMaxProfit {

    public static long[] sumSoFar = null;

    private static class Range {
        public int low;
        public int high;

        public Range(int low, int high) {
            this.low = low;
            this.high = high;
        }
    }

    private static class RangedProfit implements Comparable<RangedProfit> {
        public int low;
        public int high;
        public long profit;

        public RangedProfit(Range r) {
            this.low = r.low;
            this.high = r.high;
            profit = sumSoFar[high] - (sumSoFar[low - 1]);
        }

        @Override
        public int compareTo(RangedProfit o) {
            return Long.valueOf(o.profit).compareTo(profit);
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int nTestCases = Integer.parseInt(br.readLine());

        for (int i = 0; i < nTestCases; i++) {
            // Parse N and M
            String[] tokenArray = br.readLine().split(" ");
            int n = Integer.parseInt(tokenArray[0]);
            int m = Integer.parseInt(tokenArray[1]);

            // Parse Costs of Product
            tokenArray = br.readLine().split(" ");
            long[] costs = Arrays.stream(tokenArray).mapToLong(Long::parseLong).toArray();

            // Read L and R pairs
            List<Range> rangeList = new ArrayList<>();
            for (int j = 0; j < m; j++) {
                tokenArray = br.readLine().split(" ");
                int low = Integer.parseInt(tokenArray[0]);
                int high = Integer.parseInt(tokenArray[1]);

                rangeList.add(new Range(low, high));
            }

            // Read number of people to be selected
            int k = Integer.parseInt(br.readLine());


            MaxProfitLocation maxProfitLocation = new MaxProfitLocation(n, m, costs, rangeList, k).invoke();
            long maxProfit = maxProfitLocation.getMaxProfit();
            int shopVisitedMost = maxProfitLocation.getShopVisitedMost();

            System.out.println(maxProfit);
            System.out.println(shopVisitedMost);
        }
    }

    private static class MaxProfitLocation {
        private int n;
        private int m;
        private long[] costs;
        private List<Range> rangeList;
        private int k;

        private int shopVisitedMost;
        private long maxProfit;

        public MaxProfitLocation(int n, int m, long[] costs, List<Range> rangeList, int k) {
            this.n = n;
            this.m = m;
            this.costs = costs;
            this.rangeList = rangeList;
            this.k = k;
        }

        public int getShopVisitedMost() {
            return shopVisitedMost;
        }

        public long getMaxProfit() {
            return maxProfit;
        }

        public MaxProfitLocation invoke() {
            sumSoFar = new long[n + 1];
            long[] shopVisitRange = new long[n + 2];

            // init sum so far
            for (int j = 0; j < costs.length; j++) {
                if (j == 0) {
                    sumSoFar[j + 1] = costs[j];
                } else {
                    sumSoFar[j + 1] = sumSoFar[j] + costs[j];
                }
            }

            List<RangedProfit> rangedProfitList = new ArrayList<>();
            for (int i = 0; i < m; i++) {
                Range currRange = rangeList.get(i);
                rangedProfitList.add(new RangedProfit(currRange));
                // update shop visit range
                shopVisitRange[currRange.low]++;
                shopVisitRange[currRange.high + 1]--;
            }

            long[] shopVisitedMostArray = new long[n + 2];
            shopVisitedMostArray[0] = shopVisitRange[0];
            // Update shopvisitedMost array using range array
            for (int i = 1; i < shopVisitedMostArray.length; i++) {
                shopVisitedMostArray[i] = shopVisitRange[i] + shopVisitedMostArray[i - 1];
            }


            // Sort by profit
            Collections.sort(rangedProfitList);
            shopVisitedMost = 0;
            long maxSoFar = 0;
            // find index of max visited shop
            for (int i = 1; i < shopVisitedMostArray.length; i++) {
                if (shopVisitedMostArray[i] > maxSoFar) {
                    shopVisitedMost = i;
                    maxSoFar = shopVisitedMostArray[i];
                }
            }

            maxProfit = 0;
            for (int i = 0; i < k; i++) {
                maxProfit = maxProfit + rangedProfitList.get(i).profit;
            }
            return this;
        }
    }
}
