package com.saude.maxima;

import com.saude.maxima.Adapters.Package.Package;

import java.io.Serializable;
import java.util.List;

/**
 * Created by junnyor on 11/26/17.
 */

public class ListPackages implements Serializable {
    public List<Package> packagesList;
    public static final String key = "packages";

    public ListPackages(List<Package> packagesList){
        this.packagesList = packagesList;
    }

    public List<Package> getPackagesList(){
        return this.packagesList;
    }
}
