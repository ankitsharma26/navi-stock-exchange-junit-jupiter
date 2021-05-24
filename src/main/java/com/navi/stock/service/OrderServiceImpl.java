package com.navi.stock.service;

import java.time.LocalTime;
import java.util.PriorityQueue;

import com.navi.stock.constant.OrderType;
import com.navi.stock.entity.Order;
import com.navi.stock.exception.BadAttributeException;
import com.navi.stock.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl implements OrderService {

  private final OrderRepository orderRepository;

  @Override
  public Order addOrder(String orderLine) {
    String[] split = orderLine.split("\\s+");
    Order order = new Order();
    try {
      order.setId(Long.parseLong(split[0].substring(1)));
      order.setRequestedAt(LocalTime.parse(split[1]));

      order.setCorporationName(split[2]);

      if (split[3].equalsIgnoreCase(OrderType.BUY.name())) {
        order.setOrderType(OrderType.BUY);
      } else if (split[3].equalsIgnoreCase(OrderType.SELL.name())) {
        order.setOrderType(OrderType.SELL);
      }

      order.setRequestedPrice(Double.parseDouble(split[4]));
      order.setQuantityRequested(Long.parseLong(split[5]));

    } catch (Throwable throwable) {

      throw new BadAttributeException("Invalid Input Params");
    }

    return orderRepository.save(order);
  }

  @Override
  public void removeOrder(Order order) {
    orderRepository.delete(order);
  }


  @Override
  public PriorityQueue<Order> getPriorityOrderByCorporationAndType(String corporationName, OrderType orderType) {
    return orderRepository.findByCorporationAndOrderType(corporationName, orderType);
  }


}
