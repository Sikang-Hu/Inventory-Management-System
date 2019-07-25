# Inventory-Management-System
A course project of Database Management System

## Assumptions
- For certain items, there is a difference between timestamp of order date and arrive date
- Assume all items in certain order arrive together
- Some items will have hot season

## Features
- Basic functionalities: 
  - Add new item when an order is made to supplier: user need to specify name_of_item, name_of_supplier and other
  - Update Status: when item arrives 
- Query:
  - Inventory of certain item
  - Status of order
- Reminder: notify users if some items is going to be out of storage [ ]

## Architecture 
- IMS.Controller.IMS.Controller Java
- IMS.Model Java
- View HTML, CSS, BootStrap, React, JavaScript

## Dummy Model
There is a dummy model which is a toy version of our final model. It has only one entity ORDER,
which contains a bunch of ITEMS. An order can be initialized with a .odr file in below format:
'''
<Supplier name>
<Date>
<name> <price>
.
.
.
'''
And this model maintain a file based database, "order.dbt", in which each line contains all the
information for a single order.
