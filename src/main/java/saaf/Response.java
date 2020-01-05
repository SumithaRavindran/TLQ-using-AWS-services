package saaf;

import java.util.List;

/**
 * A basic Response object that can be consumed by FaaS Inspector
 * to be used as additional output.
 *
 * @author TCSS562 Trem_Project_GROUP8
 */
public class Response {


    @Override
    public String toString() {
        return "value=" + super.toString();
    }
    private List<String> sum;
    public List<String> average;
    public List<String> minimum;
    public List<String> maximum;
    public List<String> query_2;
    public List<String> revenue;
    public List<String> profit;
    public List<String> count;
    public List<String> where_clause;
    public List<String> groupby_clause;

    public List<String> getQuery2()
    {
        return this.query_2;
    }
    public void setQuery2(List<String> query_2)
    {
        this.query_2 = query_2;
    }

    public List<String> getSum()
    {
        return sum;
    }
    public void setSum(List<String> sum)
    {
        this.sum = sum;
    }

    public List<String> getAverage()
    {
        return this.average;
    }
    public void setAverage(List<String> average)
    {
        this.average = average;
    }

    public List<String> getMinimum()
    {
        return this.minimum;
    }
    public void setMinimum(List<String> minimum)
    {
        this.minimum = minimum;
    }

    public List<String> getMaximum()
    {
        return this.maximum;
    }
    public void setMaximum(List<String> maximum)
    {
        this.maximum = maximum;
    }

    public List<String> getRevenue()
    {
        return this.revenue;
    }
    public void setRevenue(List<String> revenue)
    {
        this.revenue = revenue;
    }

    public List<String> getProfit()
    {
        return this.profit;
    }
    public void setProfit(List<String> profit)
    {
        this.profit = profit;
    }

    public List<String> getCount()
    {
        return this.count;
    }
    public void setCount(List<String> count)
    {
        this.count = count;
    }

    public List<String> getQuery1()
    {
        return where_clause;
    }
    public void setQuery1(List<String> where_clause)
    {
        this.where_clause = where_clause;
    }

    public List<String> getQuery3()
    {
        return groupby_clause;
    }
    public void setQuery3(List<String> groupby_clause)
    {
        this.groupby_clause = groupby_clause;
    }

}
