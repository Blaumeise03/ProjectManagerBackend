package de.blaumeise03.projectmanager.utils;

import de.blaumeise03.projectmanager.data.baseData.*;
import de.blaumeise03.projectmanager.exceptions.POJOMappingException;
import de.blaumeise03.projectmanager.userManagement.User;

public abstract class POJOConverter<F, T> {
    static PlayerService playerService;

    static ItemService itemService;

    public static void setPlayerService(PlayerService playerService) {
        POJOConverter.playerService = playerService;
    }

    public static PlayerService getPlayerService() {
        return playerService;
    }

    public static ItemService getItemService() {
        return itemService;
    }

    public static void setItemService(ItemService itemService) {
        POJOConverter.itemService = itemService;
    }

    public static final POJOConverter<Object, Object> DEFAULT_CONVERTER = new POJOConverter<>() {
        @Override
        public Object convert(Object value) {
            return value;
        }
    };

    public static final POJOConverter<Integer, Integer> INTEGER_INTEGER_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public Integer convert(Integer value) {
            return value;
        }
    };

    public static final POJOConverter<Long, Long> LONG_LONG_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public Long convert(Long value) {
            return value;
        }
    };

    public static final POJOConverter<Boolean, Boolean> BOOLEAN_BOOLEAN_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public Boolean convert(Boolean value) {
            return value;
        }
    };

    public static final POJOConverter<Player, Integer> PLAYER_INTEGER_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public Integer convert(Player value) {
            if(value == null) return -1;
            if(!value.isNew())
                return value.getId();
            return null;
        }
    };

    public static final POJOConverter<Integer, Player> INTEGER_PLAYER_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public Player convert(Integer value) {
            if(value == null) return null;
            return playerService.findByID(value);
        }
    };

    public static final POJOConverter<Player, String> PLAYER_STRING_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public String convert(Player value) {
            if(value == null) return null;
            if(!value.isNew())
                return value.getName();
            return null;
        }
    };

    public static final POJOConverter<Corp, Integer> CORP_INTEGER_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public Integer convert(Corp value) {
            if(value == null) return null;
            if(!value.isNew())
                return value.getCid();
            return null;
        }
    };

    public static final POJOConverter<User, Long> USER_LONG_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public Long convert(User value) {
            if(value == null) return (long) -1;
            if(!value.isNew())
                return value.getId();
            return null;
        }
    };

    public static final POJOConverter<Item, Long> ITEM_LONG_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public Long convert(Item value) {
            if(value == null) return (long) -1;
            if(!value.isNew())
                return value.getItemID();
            return null;
        }
    };

    public static final POJOConverter<Long, Item> LONG_ITEM_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public Item convert(Long value) {
            if(value == null) return null;
            return itemService.findItemByID(value);
        }
    };

    public static final POJOConverter<Item, String> ITEM_STRING_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public String convert(Item value) {
            if(value == null) return null;
            if(!value.isNew())
                return value.getItemName();
            return null;
        }
    };

    public static final POJOConverter<Item.ItemType, String> ITEM_TYPE_STRING_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public String convert(Item.ItemType value) {
            if(value == null) return null;
            return value.name();
        }
    };

    public static final POJOConverter<String, Item.ItemType> STRING_ITEM_TYPE_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public Item.ItemType convert(String value) {
            if(value == null) return null;
            return Item.ItemType.valueOf(value);
        }
    };

    public static final POJOConverter<Price.PriceType, String> PRICE_TYPE_STRING_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public String convert(Price.PriceType value) {
            if(value == null) return null;
            return value.name();
        }
    };

    public static final POJOConverter<String, Price.PriceType> STRING_PRICE_TYPE_POJO_CONVERTER = new POJOConverter<>() {
        @Override
        public Price.PriceType convert(String value) {
            if(value == null) return null;
            return Price.PriceType.valueOf(value);
        }
    };

    public abstract T convert(F f);

    public enum DefaultConverter {
        INTEGER(Integer.class, Integer.class, INTEGER_INTEGER_POJO_CONVERTER),
        LONG(Long.class, Long.class, LONG_LONG_POJO_CONVERTER),
        BOOLEAN(Boolean.class, Boolean.class, BOOLEAN_BOOLEAN_POJO_CONVERTER),
        PLAYER_INTEGER(Player.class, Integer.class, PLAYER_INTEGER_POJO_CONVERTER),
        INTEGER_PLAYER(Integer.class, Player.class, INTEGER_PLAYER_POJO_CONVERTER),
        PLAYER_STRING(Player.class, String.class, PLAYER_STRING_POJO_CONVERTER),
        CORP_INTEGER(Corp.class, Integer.class, CORP_INTEGER_POJO_CONVERTER),
        USER_LONG(User.class, Long.class, USER_LONG_POJO_CONVERTER),
        ITEM_LONG(Item.class, Long.class, ITEM_LONG_POJO_CONVERTER),
        LONG_ITEM(Long.class, Item.class, LONG_ITEM_POJO_CONVERTER),
        ITEM_STRING(Item.class, String.class, ITEM_STRING_POJO_CONVERTER),
        ITEM_TYPE_STRING(Item.ItemType.class, String.class, ITEM_TYPE_STRING_POJO_CONVERTER),
        STRING_ITEM_TYPE(String.class, Item.ItemType.class, STRING_ITEM_TYPE_POJO_CONVERTER),
        PRICE_TYPE_STRING(Price.PriceType.class, String.class, PRICE_TYPE_STRING_POJO_CONVERTER),
        STRING_PRICE_TYPE(String.class, Price.PriceType.class, STRING_PRICE_TYPE_POJO_CONVERTER);

        POJOConverter<?, ?> converter;
        Class<?> from;
        Class<?> to;

        DefaultConverter(Class<?> from, Class<?> to, POJOConverter<?, ?> converter) {
            this.converter = converter;
            this.from = from;
            this.to = to;
        }

        @SuppressWarnings("unchecked")
        public static POJOConverter<Object, Object> getDefaultConverter(Class<?> from, Class<?> to) {
            for(DefaultConverter defaultConverter : values()) {
                if(defaultConverter.from.equals(from) && defaultConverter.to.equals(to)) return (POJOConverter<Object, Object>) defaultConverter.converter;
            }
            return DEFAULT_CONVERTER;
        }

        public static String getName(POJOConverter<?,?> pojoConverter) {
            for(DefaultConverter defaultConverter : values()) {
                if(defaultConverter.converter == pojoConverter) return defaultConverter.name();
            }
            if(DEFAULT_CONVERTER.equals(pojoConverter)) return "DEFAULT_CONVERTER";
            return null;
        }
    }
}
