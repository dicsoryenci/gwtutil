package ru.ppsrk.gwt.server.nestedset;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import ru.ppsrk.gwt.client.EqualsById;

@MappedSuperclass
@Table(indexes = { @Index(columnList = "leftnum"), @Index(columnList = "rightnum"), @Index(columnList = "depth") })
@FilterDef(name = "depthFilter", parameters = @ParamDef(name = "depth", type = "long"))
@Filter(name = "depthFilter", condition = "depth = :depth")
public class NestedSetNode extends EqualsById {
    @GeneratedValue
    @Id
    Long id;
    Long leftnum;
    Long rightnum;
    Long depth;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLeftNum() {
        return leftnum;
    }

    public void setLeftNum(Long left) {
        this.leftnum = left;
    }

    public Long getRightNum() {
        return rightnum;
    }

    public void setRightNum(Long right) {
        this.rightnum = right;
    }

    public Long getDepth() {
        return depth;
    }

    public void setDepth(Long depth) {
        this.depth = depth;
    }

}
