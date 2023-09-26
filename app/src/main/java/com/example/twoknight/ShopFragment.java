package com.example.twoknight;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.example.twoknight.standard.GameConstants;
import com.google.android.material.snackbar.Snackbar;

public class ShopFragment extends Fragment {

    private DataSaver dataSaver;
    private View rootView;

    public ShopFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        dataSaver = new DataSaver(requireContext());
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_shop, container, false);
        // Find the buttons by their IDs and set click listeners
        loadShop(rootView);
        return rootView;
    }

    private void loadShop(View rootView) {
        updateMoney();

        TextView moneyText = rootView.findViewById(R.id.moneyText);
        moneyText.setOnClickListener(this::addMoney); //TODO: Remove this at some point

        View shopView1 = rootView.findViewById(R.id.shopItem1);
        drawShopImage(R.drawable.ic_x2, shopView1);
        addItemText(R.string.power1, shopView1);
        addItemDescription("Nah", shopView1);
        addButtons(shopView1, 0);

        View shopView2 = rootView.findViewById(R.id.shopItem2);
        drawShopImage(R.drawable.ic_shield, shopView2);
        addItemText(R.string.power2, shopView2);
        addItemDescription("Choose a tile on the field and double it.\n You have n charges", shopView2);
        addButtons(shopView2, 1);

        Button menuButton = rootView.findViewById(R.id.btnMenu);
        menuButton.setOnClickListener(this::toMenu);




    }

    private void addItemDescription(String description, View shopView) {
        TextView itemDescription = shopView.findViewById(R.id.shopItemDescription);
        itemDescription.setText(description);
    }

    private void addItemText(int power, View shopView) {
        TextView shopItemTitle = shopView.findViewById(R.id.shopItemName);
        shopItemTitle.setText(power);
    }

    private static void drawShopImage(int drawableId, View shopView) {
        ImageView shopItemImage = shopView.findViewById(R.id.shopItemImage);
        shopItemImage.setImageResource(drawableId);
    }

    private void addButtons(View shopView, int itemIndex) {
        TextView quantityText = shopView.findViewById(R.id.quantityIndicator);
        String s = ""+dataSaver.loadBoughtItems()[itemIndex];
        quantityText.setText(s);
        Button buyButton = shopView.findViewById(R.id.plusButton);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                buy(itemIndex, shopView, 1);
            }
        });
        Button sellButton = shopView.findViewById(R.id.minusButton);
        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buy(itemIndex, shopView, -1);
            }
        });
    }


    private void addMoney(View view) {
        dataSaver.saveMoney(5);
        updateMoney();
    }

    private void updateMoney() {
        TextView moneyText = rootView.findViewById(R.id.moneyText);
        String moneyString = "$ " + dataSaver.loadMoney() + " $";
        moneyText.setText(moneyString);
    }

    private void toMenu(View view) {
        NavHostFragment.findNavController(ShopFragment.this)
                .navigate(R.id.action_ShopFragment_to_MenuFragment);
    }

    private void buy(int itemIndex, View shopView, int delta) {
        int money = dataSaver.loadMoney();
        int[] boughtItems = dataSaver.loadBoughtItems();
        int price = GameConstants.powerCost[itemIndex];
        if (money < delta*price){
            //Insufficient coins
            showSnackbar(shopView, "You don't have enough money");
            return;}
        if (boughtItems[itemIndex] + delta < 0){
            showSnackbar(shopView, "You don't have anything to sell");
            return;
        }
        if (boughtItems[itemIndex] + delta > GameConstants.maxItems[itemIndex]) {
            showSnackbar(shopView, "You cannot buy any more of that");
            return;
        }
        dataSaver.saveMoney(money-delta*price);
        TextView quantity = shopView.findViewById(R.id.quantityIndicator);
        boughtItems[itemIndex]+=delta; //TODO: Add try/catch?
        dataSaver.saveBoughtItems(boughtItems);
        String quanText = "" + (boughtItems[itemIndex]);
        quantity.setText(quanText);
        updateMoney();
    }

    private void showSnackbar(View button, String message) {
        Snackbar snackbar = Snackbar.make(button, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}