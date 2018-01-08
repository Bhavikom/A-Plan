Ext.define("Ext.ux.grid.FiltersFeature", {
    extend: "Ext.grid.feature.Feature",
    alias: "feature.filters",
    uses: ["Ext.ux.grid.menu.ListMenu", "Ext.ux.grid.menu.RangeMenu", "Ext.ux.grid.filter.BooleanFilter", "Ext.ux.grid.filter.DateFilter", "Ext.ux.grid.filter.ListFilter", "Ext.ux.grid.filter.NumericFilter", "Ext.ux.grid.filter.StringFilter"],
    autoReload: true,
    filterCls: "ux-filtered-column",
    local: false,
    menuFilterText: "Filters",
    paramPrefix: "filter",
    showMenu: true,
    stateId: undefined,
    updateBuffer: 500,
    hasFeatureEvent: false,
    constructor: function (e) {
        var t = this;
        e = e || {};
        Ext.apply(t, e);
        t.deferredUpdate = Ext.create("Ext.util.DelayedTask", t.reload, t);
        t.filters = t.createFiltersCollection();
        t.filterConfigs = e.filters
    },
    attachEvents: function () {
        var e = this, t = e.view, n = t.headerCt, r = e.getGridPanel();
        e.bindStore(t.getStore(), true);
        n.on("menucreate", e.onMenuCreate, e);
        t.on("refresh", e.onRefresh, e);
        r.on({scope: e, beforestaterestore: e.applyState, beforestatesave: e.saveState, beforedestroy: e.destroy});
        r.filters = e;
        r.addEvents("filterupdate")
    },
    createFiltersCollection: function () {
        return Ext.create("Ext.util.MixedCollection", false, function (e) {
            return e ? e.dataIndex : null
        })
    },
    createFilters: function () {
        function f(e, t, n) {
            if (e && (n || t)) {
                o = s.get(e);
                u = {dataIndex: e, type: o && o.type && o.type.type || "auto"};
                if (Ext.isObject(t)) {
                    Ext.apply(u, t)
                }
                r.replace(u)
            }
        }

        var e = this, t = e.filters.getCount(), n = e.getGridPanel(), r = e.createFiltersCollection(), i = n.store.model, s = i.prototype.fields, o, u, a;
        if (t) {
            a = {};
            e.saveState(null, a)
        }
        Ext.Array.each(e.filterConfigs, function (e) {
            f(e.dataIndex, e)
        });
        Ext.Array.each(n.columns, function (e) {
            if (e.filterable === false) {
                r.removeAtKey(e.dataIndex)
            } else {
                f(e.dataIndex, e.filter, e.filterable)
            }
        });
        e.removeAll();
        e.addFilters(r.items);
        if (t) {
            e.applyState(null, a)
        }
    },
    onMenuCreate: function (e, t) {
        var n = this;
        n.createFilters();
        t.on("beforeshow", n.onMenuBeforeShow, n)
    },
    onMenuBeforeShow: function (e) {
        var t = this, n, r;
        if (t.showMenu) {
            n = t.menuItem;
            if (!n || n.isDestroyed) {
                t.createMenuItem(e);
                n = t.menuItem
            }
            r = t.getMenuFilter();
            if (r) {
                n.menu = r.menu;
                n.setChecked(r.active);
                n.setDisabled(r.disabled === true)
            }
            n.setVisible(!!r);
            this.sep.setVisible(!!r)
        }
    },
    createMenuItem: function (e) {
        var t = this;
        t.sep = e.add("-");
        t.menuItem = e.add({
            checked: false,
            itemId: "filters",
            text: t.menuFilterText,
            listeners: {scope: t, checkchange: t.onCheckChange, beforecheckchange: t.onBeforeCheck}
        })
    },
    getGridPanel: function () {
        return this.view.up("gridpanel")
    },
    applyState: function (e, t) {
        var n, r;
        this.applyingState = true;
        this.clearFilters();
        if (t.filters) {
            for (n in t.filters) {
                r = this.filters.get(n);
                if (r) {
                    r.setValue(t.filters[n]);
                    r.setActive(true)
                }
            }
        }
        this.deferredUpdate.cancel();
        if (this.local) {
            this.reload()
        }
        delete this.applyingState;
        delete t.filters
    },
    saveState: function (e, t) {
        var n = {};
        this.filters.each(function (e) {
            if (e.active) {
                n[e.dataIndex] = e.getValue()
            }
        });
        return t.filters = n
    },
    destroy: function () {
        var e = this;
        Ext.destroyMembers(e, "menuItem", "sep");
        e.removeAll();
        e.clearListeners()
    },
    removeAll: function () {
        if (this.filters) {
            Ext.destroy.apply(Ext, this.filters.items);
            this.filters.clear()
        }
    },
    bindStore: function (e, t) {
        if (!t && this.store) {
            if (this.local) {
                e.un("load", this.onLoad, this)
            } else {
                e.un("beforeload", this.onBeforeLoad, this)
            }
        }
        if (e) {
            if (this.local) {
                e.on("load", this.onLoad, this)
            } else {
                e.on("beforeload", this.onBeforeLoad, this)
            }
        }
        this.store = e
    },
    getMenuFilter: function () {
        var e = this.view.headerCt.getMenu().activeHeader;
        return e ? this.filters.get(e.dataIndex) : null
    },
    onCheckChange: function (e, t) {
        this.getMenuFilter().setActive(t)
    },
    onBeforeCheck: function (e, t) {
        return !t || this.getMenuFilter().isActivatable()
    },
    onStateChange: function (e, t) {
        if (e !== "serialize") {
            var n = this, r = n.getGridPanel();
            if (t == n.getMenuFilter()) {
                n.menuItem.setChecked(t.active, false)
            }
            if ((n.autoReload || n.local) && !n.applyingState) {
                n.deferredUpdate.delay(n.updateBuffer)
            }
            n.updateColumnHeadings();
            if (!n.applyingState) {
                r.saveState()
            }
            r.fireEvent("filterupdate", n, t)
        }
    },
    onBeforeLoad: function (e, t) {
        t.params = t.params || {};
        this.cleanParams(t.params);
        var n = this.buildQuery(this.getFilterData());
        Ext.apply(t.params, n)
    },
    onLoad: function (e, t) {
        e.filterBy(this.getRecordFilter())
    },
    onRefresh: function () {
        this.updateColumnHeadings()
    },
    updateColumnHeadings: function () {
        var e = this, t = e.view.headerCt;
        if (t) {
            t.items.each(function (t) {
                var n = e.getFilter(t.dataIndex);
                t[n && n.active ? "addCls" : "removeCls"](e.filterCls)
            })
        }
    },
    reload: function () {
        var e = this, t = e.view.getStore(), n;
        if (e.local) {
            t.clearFilter(true);
            t.filterBy(e.getRecordFilter())
        } else {
            e.deferredUpdate.cancel();
            t.loadPage(1)
        }
    },
    getRecordFilter: function () {
        var e = [], t, n;
        this.filters.each(function (t) {
            if (t.active) {
                e.push(t)
            }
        });
        t = e.length;
        return function (r) {
            for (n = 0; n < t; n++) {
                if (!e[n].validateRecord(r)) {
                    return false
                }
            }
            return true
        }
    },
    addFilter: function (e) {
        var t = this.getFilterClass(e.type), n = e.menu ? e : new t(e);
        this.filters.add(n);
        Ext.util.Observable.capture(n, this.onStateChange, this);
        return n
    },
    addFilters: function (e) {
        if (e) {
            var t, n, r;
            for (t = 0, n = e.length; t < n; t++) {
                r = e[t];
                if (r) {
                    this.addFilter(r)
                }
            }
        }
    },
    getFilter: function (e) {
        return this.filters.get(e)
    },
    clearFilters: function () {
        this.filters.each(function (e) {
            e.setActive(false)
        })
    },
    getFilterData: function () {
        var e = [], t, n;
        this.filters.each(function (r) {
            if (r.active) {
                var s = [].concat(r.serialize());
                for (t = 0, n = s.length; t < n; t++) {
                    e.push({field: r.dataIndex, data: s[t]})
                }
            }
        });
        return e
    },
    buildQuery: function (e) {
        var t = {}, n, r, i, s, o, u, a = e.length;
        if (!this.encode) {
            for (n = 0; n < a; n++) {
                r = e[n];
                i = [this.paramPrefix, "[", n, "]"].join("");
                t[i + "[field]"] = r.field;
                s = i + "[data]";
                for (o in r.data) {
                    t[[s, "[", o, "]"].join("")] = r.data[o]
                }
            }
        } else {
            u = [];
            for (n = 0; n < a; n++) {
                r = e[n];
                u.push(Ext.apply({}, {field: r.field}, r.data))
            }
            if (u.length > 0) {
                t[this.paramPrefix] = Ext.JSON.encode(u)
            }
        }
        return t
    },
    cleanParams: function (e) {
        if (this.encode) {
            delete e[this.paramPrefix]
        } else {
            var t, n;
            t = new RegExp("^" + this.paramPrefix + "[[0-9]+]");
            for (n in e) {
                if (t.test(n)) {
                    delete e[n]
                }
            }
        }
    },
    getFilterClass: function (e) {
        switch (e) {
            case "auto":
                e = "string";
                break;
            case "int":
            case "float":
                e = "numeric";
                break;
            case "bool":
                e = "boolean";
                break
        }
        return Ext.ClassManager.getByAlias("gridfilter." + e)
    }
})