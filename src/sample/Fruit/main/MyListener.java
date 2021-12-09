package sample.Fruit.main;

import sample.Models.CompanyModel;
import sample.Models.Field;

import java.util.prefs.BackingStoreException;

public interface MyListener {
    public void onClickListener(CompanyModel companyModel) throws BackingStoreException;
    public void onDeleteListener(CompanyModel companyModel) throws Exception;
    public void onClickListener(Field field) throws Exception;
    public void onDeleteListener(Field field) throws Exception;
}
