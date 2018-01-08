//Customize Task Context Menu
Ext.define("Gnt.plugin.TaskContextMenu", {
    extend: "Ext.menu.Menu",
    requires: [  'Gnt.model.Dependency' ],
    plain: true,
    triggerEvent: 'taskcontextmenu',
    texts: {
        cut: 'Cut',
        copy: 'Copy',
        paste: 'Paste',
        critical: 'Critical',
        reshow: 'Reshow',
        completed: 'Completed',
        notclear: 'Not Clear',
        noticearrow: 'Notice arrow',
        passive: 'Passive',
        cancelled: 'Cancelled',
        donotfilter: 'Do not filter',
        lock: 'Lock',
        masterlock: 'Master lock',
        pagebreak: 'Page break',
        newTaskText: 'Add Component',
        newMilestoneText: 'New milestone',
        deleteTask: 'Delete Component(s)',
        editLeftLabel: 'Edit left label',
        editRightLabel: 'Edit right label',
        convert: 'Convert',
        converToMilestone: 'Convert to Milestone',
        converToRegularTask: 'Convert to Regular Task',
        add: 'Add',
        deleteDependency: 'Delete dependency',
        //addTaskAbove: 'Task above',
        addTaskBelow: 'Same Level',
        addSubtask: 'on next lower level',
        OnLevel1: 'OnLevel1(Folder)',
        OnLevel2: 'OnLevel2(Project)',
        OnLevel3: 'OnLevel3(Task)',
        addMilestone: 'Milestone',
        addSuccessor: 'Successor',
        addPredecessor: 'Predecessor'

    },
    Id: 'TaskContextMenu',
    grid: null,
    rec: null,
    lastHighlightedItem: null,
    createMenuItems: function () {
        var texts = this.texts;

        return [
            {
                itemId: 'contextmenu-Cut',
                handler: this.cut,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Cut.png',
                text: texts.cut
            },
            {
                itemId: 'contextmenu-Copy',
                handler: this.copy,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Copy.png',
                text: texts.copy
            },
            {
                itemId: 'contextmenu-Paste',
                handler: this.paste,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/icon_paste.png',
                text: texts.paste
            },
            {
                itemId: 'contextmenu-Critical',
                handler: this.critical,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/17_flash_16.png',
                text: texts.critical
            },
            {
                itemId: 'contextmenu-Reshow',
                handler: this.reshow,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/19_warning_16.png',
                text: texts.reshow
            },
            {
                itemId: 'contextmenu-Completed',
                handler: this.completed,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/10_check_16.png',
                text: texts.completed
            },
            {
                itemId: 'contextmenu-NotClear',
                handler: this.notclear,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/18_question_16.png',
                text: texts.notclear
            },
            {
                itemId: 'contextmenu-NoticeArrow',
                handler: this.noticearrow,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/16_arrow_16.png',
                text: texts.noticearrow
            },
            {
                itemId: 'contextmenu-Passive',
                handler: this.passive,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/11_task_passive_16.png',
                text: texts.passive
            },
            {
                itemId: 'contextmenu-Cancelled',
                handler: this.cancelled,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/12_task_canceled_16.png',
                text: texts.cancelled
            },
            {
                itemId: 'contextmenu-DoNotFilter',
                handler: this.donotfilter,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/DonotFilter.png',
                text: texts.donotfilter
            },
            {
                itemId: 'contextmenu-Lock',
                handler: this.lock,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Lock.png',
                text: texts.lock
            },
            {
                itemId: 'contextmenu-MasterLock',
                handler: this.masterlock,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/MasterLock.png',
                text: texts.masterlock
            },
            {
                itemId: 'contextmenu-PageBreak',
                handler: this.pagebreak,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/PageBreak.png',
                text: texts.pagebreak
            },
            {
                itemId: 'contextmenu-DeleteTask',
                handler: this.deleteTask,
                requiresTask: true,
                scope: this,
                icon: 'Ribbon/images/Icons/12_task_canceled_16.png',
                text: texts.deleteTask
            },
            {
                itemId: 'contextmenu-EditLeftLable',
                handler: this.editLeftLabel,
                requiresTask: true,
                scope: this,
                text: texts.editLeftLabel
            },
            {
                itemId: 'contextmenu-EditRightLable',
                handler: this.editRightLabel,
                requiresTask: true,
                scope: this,
                text: texts.editRightLabel
            },
            {
                text: texts.convert,
                itemId: 'contextmenu-Convert',
                menu: {
                    plain: true,
                    items: [
                        {
                            itemId: 'contextmenu-ConvertToMilestone',
                            handler: this.converToMilestone,
                            requiresTask: true,
                            scope: this,
                            text: texts.converToMilestone
                        },
                        {
                            itemId: 'contextmenu-ConvertToRegularTask',
                            handler: this.converToRegularTask,
                            requiresTask: true,
                            scope: this,
                            text: texts.converToRegularTask
                        }
                    ]
                }
            },
            {
                text: texts.add,
                itemId: 'contextmenu-Add',
                menu: {
                    plain: true,
                    items: [
                        {
                            itemId: 'contextmenu-OnLevel1',
                            handler: this.OnLevel1Action,
                            scope: this,
                            text: texts.OnLevel1
                        },
                        {
                            itemId: 'contextmenu-OnLevel2',
                            handler: this.OnLevel2Action,
                            requiresTask: true,
                            scope: this,
                            text: texts.OnLevel2
                        },
                        {
                            itemId: 'contextmenu-OnLevel3',
                            handler: this.OnLevel3Action,
                            requiresTask: true,
                            scope: this,
                            text: texts.OnLevel3
                        },
                        //{
                        //    handler: this.addTaskAboveAction,
                        //    requiresTask: true,
                        //    scope: this,
                        //    text: texts.addTaskAbove
                        //},
                        {
                            itemId: 'contextmenu-AddTaskBelow',
                            handler: this.addTaskBelowAction,
                            requiresTask: true,
                            scope: this,
                            text: texts.addTaskBelow
                        },
                        {
                            itemId: 'contextmenu-AddSubTask',
                            handler: this.addSubtask,
                            requiresTask: true,
                            scope: this,
                            text: texts.addSubtask
                        },
                        {
                            itemId: 'contextmenu-AddMilsestone',
                            handler: this.addMilestone,
                            requiresTask: true,
                            scope: this,
                            text: texts.addMilestone
                        },
                        {
                            itemId: 'contextmenu-AddSuccessor',
                            handler: this.addSuccessor,
                            requiresTask: true,
                            scope: this,
                            text: texts.addSuccessor
                        },
                        {
                            itemId: 'contextmenu-AddPredecessor',
                            handler: this.addPredecessor,
                            requiresTask: true,
                            scope: this,
                            text: texts.addPredecessor
                        }
                    ]
                }
            },
            {
                itemId: 'contextmenu-deleteDependency',
                text: texts.deleteDependency,
                requiresTask: true,
                menu: {
                    plain: true,

                    listeners: {
                        beforeshow: this.populateDependencyMenu,

                        // highlight dependencies on mouseover of the menu item
                        mouseover: this.onDependencyMouseOver,

                        // unhighlight dependencies on mouseout of the menu item
                        mouseleave: this.onDependencyMouseOut,

                        scope: this
                    }
                }
            }
        ];
    },
    listeners: {
        'beforeshow': function () {
            var me = this;
            var tasks = this.grid.getSelectionModel().selected;
            var a = me.getComponent("contextmenu-Add");
            if (tasks.length > 0) {
                var rawId = tasks.items[0].data.Id;
                var level = tasks.items[0].data.Outline_Level;
                if (tasks.items[0].data.Outline_Level == 1) {
                    me.getComponent("contextmenu-Cut").show();
                    me.getComponent("contextmenu-Copy").show();
                    me.getComponent("contextmenu-Paste").show();
                    me.getComponent("contextmenu-Critical").hide();
                    me.getComponent("contextmenu-Reshow").hide();
                    me.getComponent("contextmenu-Completed").hide();
                    me.getComponent("contextmenu-NotClear").hide();
                    me.getComponent("contextmenu-NoticeArrow").hide();
                    me.getComponent("contextmenu-Passive").hide();
                    me.getComponent("contextmenu-Cancelled").show();
                    me.getComponent("contextmenu-Lock").show();
                    me.getComponent("contextmenu-MasterLock").hide();
                    me.getComponent("contextmenu-DoNotFilter").hide();
                    me.getComponent("contextmenu-DeleteTask").show();
                    me.getComponent("contextmenu-EditLeftLable").hide();
                    me.getComponent("contextmenu-EditRightLable").hide();
                    me.getComponent("contextmenu-Convert").hide();
                    if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                        a.menu.items.items[0].show();
                    }
                    if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                        a.menu.items.items[1].show();
                    }
                    if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                        a.menu.items.items[2].hide();
                    }
                    if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                        a.menu.items.items[3].show();
                    }
                    if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                        a.menu.items.items[4].show();
                    }
                    if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                        a.menu.items.items[5].hide();
                    }
                    if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                        a.menu.items.items[6].hide();
                    }
                    if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                        a.menu.items.items[7].hide();
                    }
                    me.getComponent("contextmenu-deleteDependency").hide();

                    if (tasks.items[0].data.Lock == 1) {
                        me.getComponent("contextmenu-Cut").hide();
                        me.getComponent("contextmenu-Copy").show();
                        me.getComponent("contextmenu-Paste").show();
                        me.getComponent("contextmenu-Critical").hide();
                        me.getComponent("contextmenu-Reshow").hide();
                        me.getComponent("contextmenu-Completed").hide();

                        me.getComponent("contextmenu-NotClear").hide();
                        me.getComponent("contextmenu-NoticeArrow").hide();
                        me.getComponent("contextmenu-Passive").hide();

                        me.getComponent("contextmenu-Cancelled").show();
                        me.getComponent("contextmenu-Lock").show();
                        me.getComponent("contextmenu-MasterLock").hide();
                        me.getComponent("contextmenu-DoNotFilter").hide();

                        me.getComponent("contextmenu-DeleteTask").hide();

                        me.getComponent("contextmenu-EditLeftLable").hide();
                        me.getComponent("contextmenu-EditRightLable").hide();
                        me.getComponent("contextmenu-Convert").hide();
                        if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                            a.menu.items.items[0].show();
                        }
                        if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                            a.menu.items.items[1].hide();
                        }
                        if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                            a.menu.items.items[2].hide();
                        }
                        if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                            a.menu.items.items[3].show();
                        }
                        if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                            a.menu.items.items[4].hide();
                        }
                        if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                            a.menu.items.items[5].hide();
                        }
                        if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                            a.menu.items.items[6].hide();
                        }
                        if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                            a.menu.items.items[7].hide();
                        }
                        me.getComponent("contextmenu-deleteDependency").hide();
                    }
                    $.ajax({
                        type: 'GET',
                        url: "Tasks/CheckPermission?rawId=" + rawId + '&level=' + level,
                        autoSync: true,
                        autoLoad: true,
                        success: function (response) {

                            if (response == "Read" || response == "Change") {
                                me.getComponent("contextmenu-Cut").hide();
                                var a = me.getComponent("contextmenu-Add");
                                if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                    a.menu.items.items[1].hide();
                                }
                                if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                    a.menu.items.items[2].hide();
                                }
                                if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                    a.menu.items.items[3].show();
                                }
                                if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                    a.menu.items.items[4].hide();
                                }
                                me.getComponent("contextmenu-DeleteTask").hide();
                            }
                        }
                    });
                }
                else if (tasks.items[0].data.Outline_Level == 2) {
                    me.getComponent("contextmenu-Cut").show();
                    me.getComponent("contextmenu-Copy").show();
                    me.getComponent("contextmenu-Paste").show();
                    me.getComponent("contextmenu-Critical").show();
                    me.getComponent("contextmenu-Reshow").show();
                    me.getComponent("contextmenu-Completed").show();

                    me.getComponent("contextmenu-NotClear").show();
                    me.getComponent("contextmenu-NoticeArrow").show();
                    me.getComponent("contextmenu-Passive").show();

                    me.getComponent("contextmenu-Cancelled").show();
                    me.getComponent("contextmenu-DoNotFilter").show();

                    me.getComponent("contextmenu-DeleteTask").show();

                    me.getComponent("contextmenu-EditLeftLable").show();
                    me.getComponent("contextmenu-EditRightLable").show();
                    me.getComponent("contextmenu-Convert").show();

                    if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                        a.menu.items.items[0].show();
                    }
                    if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                        a.menu.items.items[1].show();
                    }
                    if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                        a.menu.items.items[2].show();
                    }
                    if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                        a.menu.items.items[3].show();
                    }
                    if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                        a.menu.items.items[4].show();
                    }
                    if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                        a.menu.items.items[5].show();
                    }
                    if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                        a.menu.items.items[6].show();
                    }
                    if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                        a.menu.items.items[7].show();
                    }
                    me.getComponent("contextmenu-deleteDependency").show();

                    if (tasks.items[0].data.Lock == 1) {
                        me.getComponent("contextmenu-Cut").hide();
                        me.getComponent("contextmenu-Copy").show();
                        me.getComponent("contextmenu-Paste").show();
                        me.getComponent("contextmenu-Critical").hide();
                        me.getComponent("contextmenu-Reshow").hide();
                        me.getComponent("contextmenu-Completed").hide();

                        me.getComponent("contextmenu-NotClear").hide();
                        me.getComponent("contextmenu-NoticeArrow").hide();
                        me.getComponent("contextmenu-Passive").hide();

                        me.getComponent("contextmenu-Cancelled").show();
                        me.getComponent("contextmenu-Lock").show();
                        me.getComponent("contextmenu-MasterLock").hide();
                        me.getComponent("contextmenu-DoNotFilter").hide();

                        me.getComponent("contextmenu-DeleteTask").hide();

                        me.getComponent("contextmenu-EditLeftLable").hide();
                        me.getComponent("contextmenu-EditRightLable").hide();
                        me.getComponent("contextmenu-Convert").hide();
                        if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                            a.menu.items.items[0].show();
                        }
                        if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                            a.menu.items.items[1].hide();
                        }
                        if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                            a.menu.items.items[2].hide();
                        }
                        if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                            a.menu.items.items[3].show();
                        }
                        if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                            a.menu.items.items[4].hide();
                        }
                        if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                            a.menu.items.items[5].hide();
                        }
                        if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                            a.menu.items.items[6].hide();
                        }
                        if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                            a.menu.items.items[7].hide();
                        }
                        me.getComponent("contextmenu-deleteDependency").hide();
                    }
                    $.ajax({
                        type: 'GET',
                        url: "Tasks/CheckPermission?rawId=" + rawId + '&level=' + level,
                        autoSync: true,
                        autoLoad: true,
                        success: function (response) {
                            var a = me.getComponent("contextmenu-Add");
                            if (response == "Change") {
                                me.getComponent("contextmenu-Cut").hide();
                                if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                    a.menu.items.items[1].hide();
                                }
                                if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                    a.menu.items.items[2].hide();
                                }
                                if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                    a.menu.items.items[3].show();
                                }
                                if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                    a.menu.items.items[4].hide();
                                }
                                if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                    a.menu.items.items[5].hide();
                                }
                                if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                    a.menu.items.items[6].hide();
                                }
                                if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                    a.menu.items.items[7].hide();
                                }
                                me.getComponent("contextmenu-DeleteTask").hide();
                            }
                            else if (response == "Read") {
                                me.getComponent("contextmenu-Cut").hide();
                                me.getComponent("contextmenu-Copy").show();
                                me.getComponent("contextmenu-Paste").show();
                                me.getComponent("contextmenu-Critical").hide();
                                me.getComponent("contextmenu-Reshow").hide();
                                me.getComponent("contextmenu-Completed").hide();

                                me.getComponent("contextmenu-NotClear").hide();
                                me.getComponent("contextmenu-NoticeArrow").hide();
                                me.getComponent("contextmenu-Passive").hide();

                                me.getComponent("contextmenu-Cancelled").hide();
                                me.getComponent("contextmenu-Lock").show();
                                me.getComponent("contextmenu-MasterLock").show();
                                me.getComponent("contextmenu-DoNotFilter").hide();

                                me.getComponent("contextmenu-DeleteTask").hide();

                                me.getComponent("contextmenu-EditLeftLable").hide();
                                me.getComponent("contextmenu-EditRightLable").hide();
                                me.getComponent("contextmenu-Convert").hide();
                                if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                                    a.menu.items.items[0].show();
                                }
                                if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                    a.menu.items.items[1].hide();
                                }
                                if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                    a.menu.items.items[2].hide();
                                }
                                if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                    a.menu.items.items[3].show();
                                }
                                if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                    a.menu.items.items[4].hide();
                                }
                                if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                    a.menu.items.items[5].hide();
                                }
                                if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                    a.menu.items.items[6].hide();
                                }
                                if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                    a.menu.items.items[7].hide();
                                }
                                me.getComponent("contextmenu-deleteDependency").hide();
                            }
                        }
                    });
                } else if (tasks.items[0].data.Outline_Level == 3) {
                    if (tasks.items[0].data.Lock == 1) {
                        me.getComponent("contextmenu-Cut").hide();
                        me.getComponent("contextmenu-Copy").show();
                        me.getComponent("contextmenu-Paste").show();
                        me.getComponent("contextmenu-Critical").hide();
                        me.getComponent("contextmenu-Reshow").hide();
                        me.getComponent("contextmenu-Completed").hide();

                        me.getComponent("contextmenu-NotClear").hide();
                        me.getComponent("contextmenu-NoticeArrow").hide();
                        me.getComponent("contextmenu-Passive").hide();

                        me.getComponent("contextmenu-Cancelled").show();
                        me.getComponent("contextmenu-Lock").show();
                        me.getComponent("contextmenu-MasterLock").hide();
                        me.getComponent("contextmenu-DoNotFilter").hide();

                        me.getComponent("contextmenu-DeleteTask").hide();

                        me.getComponent("contextmenu-EditLeftLable").hide();
                        me.getComponent("contextmenu-EditRightLable").hide();
                        me.getComponent("contextmenu-Convert").hide();
                        if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                            a.menu.items.items[0].show();
                        }
                        if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                            a.menu.items.items[1].hide();
                        }
                        if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                            a.menu.items.items[2].hide();
                        }
                        if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                            a.menu.items.items[3].show();
                        }
                        if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                            a.menu.items.items[4].hide();
                        }
                        if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                            a.menu.items.items[5].hide();
                        }
                        if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                            a.menu.items.items[6].hide();
                        }
                        if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                            a.menu.items.items[7].hide();
                        }
                        me.getComponent("contextmenu-deleteDependency").hide();
                    } else {
                        $.ajax({
                            type: 'GET',
                            url: "Tasks/CheckPermission?rawId=" + tasks.items[0].data.parentId + '&level=' + level,
                            autoSync: true,
                            autoLoad: true,
                            success: function (response) {
                                var a = me.getComponent("contextmenu-Add");
                                if (response == "Change") {
                                    me.getComponent("contextmenu-Cut").hide();
                                    me.getComponent("contextmenu-Copy").show();
                                    me.getComponent("contextmenu-Paste").show();

                                    me.getComponent("contextmenu-Critical").show();
                                    me.getComponent("contextmenu-Reshow").show();
                                    me.getComponent("contextmenu-Completed").show();

                                    me.getComponent("contextmenu-NotClear").show();
                                    me.getComponent("contextmenu-NoticeArrow").show();
                                    me.getComponent("contextmenu-Passive").show();

                                    me.getComponent("contextmenu-Cancelled").show();
                                    me.getComponent("contextmenu-DoNotFilter").show();
                                    me.getComponent("contextmenu-Lock").show();
                                    me.getComponent("contextmenu-MasterLock").show();

                                    me.getComponent("contextmenu-EditLeftLable").show();
                                    me.getComponent("contextmenu-EditRightLable").show();
                                    me.getComponent("contextmenu-Convert").show();

                                    if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                                        a.menu.items.items[0].show();
                                    }
                                    if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                        a.menu.items.items[1].hide();
                                    }
                                    if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                        a.menu.items.items[2].hide();
                                    }
                                    if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                        a.menu.items.items[3].show();
                                    }
                                    if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                        a.menu.items.items[4].hide();
                                    }
                                    if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                        a.menu.items.items[5].hide();
                                    }
                                    if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                        a.menu.items.items[6].hide();
                                    }
                                    if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                        a.menu.items.items[7].hide();
                                    }
                                    me.getComponent("contextmenu-deleteDependency").hide();
                                    me.getComponent("contextmenu-DeleteTask").hide();
                                }
                                if (response == "Read") {
                                    me.getComponent("contextmenu-Cut").hide();
                                    me.getComponent("contextmenu-Copy").show();
                                    me.getComponent("contextmenu-Paste").show();
                                    me.getComponent("contextmenu-Critical").hide();
                                    me.getComponent("contextmenu-Reshow").hide();
                                    me.getComponent("contextmenu-Completed").hide();

                                    me.getComponent("contextmenu-NotClear").hide();
                                    me.getComponent("contextmenu-NoticeArrow").hide();
                                    me.getComponent("contextmenu-Passive").hide();

                                    me.getComponent("contextmenu-Cancelled").hide();
                                    me.getComponent("contextmenu-DoNotFilter").hide();
                                    me.getComponent("contextmenu-Lock").show();
                                    me.getComponent("contextmenu-MasterLock").show();

                                    me.getComponent("contextmenu-DeleteTask").hide();

                                    me.getComponent("contextmenu-EditLeftLable").hide();
                                    me.getComponent("contextmenu-EditRightLable").hide();
                                    me.getComponent("contextmenu-Convert").hide();
                                    if (a.menu.items.items[0].itemId == "contextmenu-OnLevel1") {
                                        a.menu.items.items[0].show();
                                    }
                                    if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                        a.menu.items.items[1].hide();
                                    }
                                    if (a.menu.items.items[2].itemId == "contextmenu-OnLevel3") {
                                        a.menu.items.items[2].hide();
                                    }
                                    if (a.menu.items.items[3].itemId == "contextmenu-AddTaskBelow") {
                                        a.menu.items.items[3].show();
                                    }
                                    if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                        a.menu.items.items[4].hide();
                                    }
                                    if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                        a.menu.items.items[5].hide();
                                    }
                                    if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                        a.menu.items.items[6].hide();
                                    }
                                    if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                        a.menu.items.items[7].hide();
                                    }
                                    me.getComponent("contextmenu-deleteDependency").hide();
                                }
                                if (response == "Create" || response == "Standard") {
                                    me.getComponent("contextmenu-Cut").show();
                                    me.getComponent("contextmenu-Copy").show();
                                    me.getComponent("contextmenu-Paste").show();
                                    me.getComponent("contextmenu-Critical").show();
                                    me.getComponent("contextmenu-Reshow").show();
                                    me.getComponent("contextmenu-Completed").show();

                                    me.getComponent("contextmenu-NotClear").show();
                                    me.getComponent("contextmenu-NoticeArrow").show();
                                    me.getComponent("contextmenu-Passive").show();

                                    me.getComponent("contextmenu-Cancelled").show();
                                    me.getComponent("contextmenu-DoNotFilter").show();

                                    me.getComponent("contextmenu-DeleteTask").show();

                                    me.getComponent("contextmenu-EditLeftLable").show();
                                    me.getComponent("contextmenu-EditRightLable").show();
                                    me.getComponent("contextmenu-Convert").show();

                                    if (a.menu.items.items[0].itemId == "contextmenu-OnLevel") {
                                        a.menu.items.items[0].show();
                                    }
                                    if (a.menu.items.items[1].itemId == "contextmenu-OnLevel2") {
                                        a.menu.items.items[1].show();
                                    }
                                    if (a.menu.items.items[2].itemId == "contextmenu-AddTaskBelow") {
                                        a.menu.items.items[2].show();
                                    }
                                    if (a.menu.items.items[3].itemId == "contextmenu-AddSubTask") {
                                        a.menu.items.items[3].show();
                                    }
                                    if (a.menu.items.items[4].itemId == "contextmenu-AddSubTask") {
                                        a.menu.items.items[4].show();
                                    }
                                    if (a.menu.items.items[5].itemId == "contextmenu-AddMilsestone") {
                                        a.menu.items.items[5].show();
                                    }
                                    if (a.menu.items.items[6].itemId == "contextmenu-AddSuccessor") {
                                        a.menu.items.items[6].show();
                                    }
                                    if (a.menu.items.items[7].itemId == "contextmenu-AddPredecessor") {
                                        a.menu.items.items[7].show();
                                    }
                                    me.getComponent("contextmenu-deleteDependency").show();
                                }
                            },
                            error: function () {
                                //alert("Request Failed");
                            },
                            processData: true,
                            async: true
                        });
                    }
                }
            }
        }
    },

    // backward compat
    buildMenuItems: function () {
        this.items = this.createMenuItems();
    },

    initComponent: function () {
        this.buildMenuItems();
        this.callParent(arguments);
    },

    init: function (grid) {
        grid.on('destroy', this.cleanUp, this);
        var scheduleView = grid.getSchedulingView(),
            lockedView = grid.lockedGrid.getView();

        //TODO::This Comment is only for readonly mode

        if (this.triggerEvent === 'itemcontextmenu') {
            lockedView.on('itemcontextmenu', this.onItemContextMenu, this);
            scheduleView.on('itemcontextmenu', this.onItemContextMenu, this);
        } else {
            //get task context menu on both left and right grid click event
            scheduleView.on('taskcontextmenu', this.onTaskContextMenu, this);
            lockedView.on('itemcontextmenu', this.onItemContextMenu, this);
        }

        // Handle case of empty schedule too
        scheduleView.on('containercontextmenu', this.onContainerContextMenu, this);
        lockedView.on('containercontextmenu', this.onContainerContextMenu, this);
        this.grid = grid;
    },

    populateDependencyMenu: function (menu) {
        var grid = this.grid,
            taskStore = grid.getTaskStore(),
            dependencies = this.rec.getAllDependencies(),
            depStore = grid.dependencyStore;

        menu.removeAll();

        if (dependencies.length === 0) {
            return false;
        }

        var taskId = this.rec.getId() || this.rec.internalId;

        Ext.each(dependencies, function (dependency) {
            var fromId = dependency.get('From'),
                task = taskStore.getById(fromId == taskId ? dependency.get('To') : fromId);

            if (task) {
                menu.add({
                    depId: dependency.internalId,
                    text: Ext.util.Format.ellipsis(task.get('Name'), 30),

                    scope: this,
                    handler: function (menuItem) {
                        // in 4.0.2 `indexOfId` returns the record by the `internalId`
                        // in 4.0.7 `indexOfId` returns the record by its "real" id
                        // so need to manually scan the store to find the record

                        var record;

                        depStore.each(function (dependency) {
                            if (dependency.internalId == menuItem.depId) { record = dependency; return false; }
                        });

                        depStore.remove(record);
                    }
                });
            }
        }, this);
    },

    onDependencyMouseOver: function (menu, item, e) {
        if (item) {
            var schedulingView = this.grid.getSchedulingView();

            if (this.lastHighlightedItem) {
                schedulingView.unhighlightDependency(this.lastHighlightedItem.depId);
            }

            this.lastHighlightedItem = item;
            schedulingView.highlightDependency(item.depId);
        }
    },

    onDependencyMouseOut: function (menu, e) {
        if (this.lastHighlightedItem) {
            this.grid.getSchedulingView().unhighlightDependency(this.lastHighlightedItem.depId);
        }
    },

    cleanUp: function () {
        if (this.menu) {
            this.menu.destroy();
        }
    },

    onTaskContextMenu: function (g, record, e) {
        this.activateMenu(record, e);
    },

    onItemContextMenu: function (view, record, item, index, e) {
        this.activateMenu(record, e);
    },

    onContainerContextMenu: function (g, e) {
        this.activateMenu(null, e);
    },

    activateMenu: function (rec, e) {
        e.stopEvent();

        this.rec = rec;
        var reqTasks = this.query('[requiresTask]');
        Ext.each(reqTasks, function (item) {
            item.setDisabled(!rec);
        });
        this.showAt(e.getXY());
    },

    copyTask: function (originalRecord) {
        if (originalRecord != null) {
            var taskStore = this.grid.getTaskStore(),

                newTask = new taskStore.model({
                    PercentDone: 0,
                    Name: this.texts.newTaskText,
                    StartDate: originalRecord.get('StartDate'),
                    EndDate: originalRecord.get('EndDate'),
                    parentId: originalRecord.get('parentId') + 1,
                    leaf: true,
                    Outline_Level: originalRecord.get('Outline_Level')
                });
            return newTask;
        } else {
            var taskStore = this.grid.getTaskStore(),

                newTask = new taskStore.model({
                    leaf: true,
                    Id: null,
                    StartDate: null,
                    EndDate: null,
                    Priority: 1,
                    parentId: null,
                    isCriticalTask: false,
                    isClarificationRequired: false,
                    LimitDate: null,
                    isNotice: false,
                    isReshow: false,
                    isCompleted: false,
                    isPassive: false,
                    isCancelled: false,
                    index: 32,
                    Cls: "",
                    Name: "New task",
                    Duration: null,
                    Effort: null,
                    EffortUnit: "h",
                    CalendarId: "",
                    Note: "",
                    PercentDone: 0,
                    ManuallyScheduled: false,
                    SchedulingMode: "Normal",
                    BaselineStartDate: null,
                    BaselineEndDate: null,
                    BaselinePercentDone: 0,
                    PhantomId: "ext-record-363",
                    PhantomParentId: "",
                    DurationUnit: "d"
                });
            return newTask;
        }
    },

    addTaskAbove: function (newTask) {
        var task = this.rec;
        newTask.isCriticalTask = false;
        newTask.isClarificationRequired = false;
        newTask.isPassive = false;
        newTask.isCancelled = false;
        newTask.isNotice = false;
        newTask.isCompleted = false;
        newTask.isReshow = false;
        newTask.NoticeStatus = 3;
        newTask.LastModification = new Date();
        newTask.LastUserMod = new Date();
        newTask.Group_Name = task.parentNode.data.Name;
        newTask.Created = new Date();
        if (task.data.Outline_Level == 1) {
            newTask.Name = '(Project desig.)';
        }
        else if (task.data.Outline_Level >= 2) {
            newTask.Name = '(Task desig.)';
        }
        task.parentNode.set('Object', parseInt(task.parentNode.data.Object) + 1);
        if (task) {
            task.parentNode.insertBefore(newTask, task);
        } else {
            this.grid.taskStore.getRootNode().appendChild(newTask);
        }
    },

    addTaskBelow: function (newTask) {
        var task = this.rec;
        console.log(task.parentNode);
        if (task.data.Outline_Level == 1) {
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.data.Name;
            newTask.Created = new Date();
            newTask.set('Outline_Level', 1);
            newTask.set('leaf', true);
            newTask.set('Name', '(Folder desig.)');
            newTask.set('parentId', 0);
            task.parentNode.set('Object', parseInt(task.parentNode.data.Object) + 1);
            if (task) {
                task.parentNode.insertBefore(newTask, task.nextSibling);
            } else {
                this.grid.taskStore.getRootNode().appendChild(newTask);
            }
        }
        else if (task.data.Outline_Level == 2) {
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.data.Name;
            newTask.Created = new Date();
            newTask.set('Outline_Level', 2);
            newTask.set('Name', '(Project desig.)');
            newTask.set('leaf', true);
            task.parentNode.set('Object', parseInt(task.parentNode.data.Object) + 1);
            if (task) {
                task.parentNode.insertBefore(newTask, task.nextSibling);
            } else {
                this.grid.taskStore.getRootNode().appendChild(newTask);
            }
        } else {
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.data.Name;
            newTask.Created = new Date();
            newTask.set('Outline_Level', task.data.Outline_Level);
            newTask.set('Name', '(Task desig.)');
            newTask.set('leaf', task.data.leaf);
            task.parentNode.set('Object', parseInt(task.parentNode.data.Object) + 1);
            if (task) {
                task.parentNode.insertBefore(newTask, task.nextSibling);
            } else {
                this.grid.taskStore.getRootNode().appendChild(newTask);
            }
        }
    },

    critical: function () {
        var task = this.rec;
        if (task.data.isCriticalTask == true) {
            task.set('isCriticalTask', false);
        } else {
            task.set('isCriticalTask', true);
        }
    },

    reshow: function () {
        var task = this.rec;
        if (task.data.isReshow == true) {
            task.data.Reshow_Date = null;
            task.set('isReshow', false);
        } else {
            reshowDateWindow.show();
        }
    },

    completed: function () {
        var task = this.rec;
        Ext.defer(function () {
            /*statusBar.setStatus("Ready");*/
            if (task.data.isCompleted != true) {
                makeCompletedTasks(task);
                task.set('PercentDone', 100);
                task.set('isCompleted', true);
                task.set('Completed', new Date());
            } else {
                makeDefaultTasks(task);
                task.set('PercentDone', 0);
                task.set('isCompleted', false);
                task.set('Completed', null);
            }
            function makeCompletedTasks(node) {
                for (var i = 0; i < node.childNodes.length; i++) {
                    node.expand();
                    var child = node.childNodes[i];
                    makeCompletedTasks(child);
                    completedTasks(child);
                }
            }
            function makeDefaultTasks(node) {
                for (var i = 0; i < node.childNodes.length; i++) {
                    node.expand();
                    var child = node.childNodes[i];
                    makeDefaultTasks(child);
                    defaultTasks(child);
                }
            }

            function completedTasks(child) {
                child.set('PercentDone', 100);
                child.set('isCompleted', true);
                child.set('Completed', new Date());
            }
            function defaultTasks(child) {
                child.set('PercentDone', 0);
                child.set('isCompleted', false);
                child.set('Completed', null);
            }
        }, 1000);
    },

    notclear: function () {
        var task = this.rec;
        if (task.data.isClarificationRequired == true) {
            task.set('isClarificationRequired', false);
        } else {
            task.set('isClarificationRequired', true);
        }
    },

    noticearrow: function () {
        var task = this.rec;
        if (task.data.isNotice == true) {
            task.set('isNotice', false);
        } else {
            task.set('isNotice', true);
        }
    },

    passive: function () {
        var task = this.rec;
        task.expand();
        Ext.defer(function () {
            //statusBar.setStatus("Ready");
            if (task.data.isPassive != true) {
                makeisPassiveTasks(task);
                task.set('isPassive', true);
                task.set('isCancelled', false);
            } else {
                makeDefaultTasks(task);
                task.set('isPassive', false);
            }
            function makeisPassiveTasks(node) {
                for (var i = 0; i < node.childNodes.length; i++) {
                    node.expand();
                    var child = node.childNodes[i];
                    makeisPassiveTasks(child);
                    passiveTasks(child);
                }
            }
            function makeDefaultTasks(node) {
                for (var i = 0; i < node.childNodes.length; i++) {
                    node.expand();
                    var child = node.childNodes[i];
                    makeDefaultTasks(child);
                    defaultTasks(child);
                }
            }
            function passiveTasks(child) {
                child.set('isPassive', true);
                child.set('isCancelled', false);
            }
            function defaultTasks(child) {
                child.set('isPassive', false);
            }
        }, 1000);
    },

    cancelled: function () {
        var task = this.rec;
        Ext.defer(function () {
            //statusBar.setStatus("Ready");
            if (task.data.isCancelled != true) {
                makeCancelledTasks(task);
                task.set('isCancelled', true);
                task.set('isPassive', false);
            } else {
                makeDefaultTasks(task);
                task.set('isCancelled', false);
            }
            function makeCancelledTasks(node) {
                for (var i = 0; i < node.childNodes.length; i++) {
                    node.expand();
                    var child = node.childNodes[i];
                    makeCancelledTasks(child);
                    cancelledTasks(child);
                }
            }
            function makeDefaultTasks(node) {
                for (var i = 0; i < node.childNodes.length; i++) {
                    node.expand();
                    var child = node.childNodes[i];
                    makeDefaultTasks(child);
                    defaultTasks(child);
                }
            }
            function cancelledTasks(child) {
                child.set('isCancelled', true);
                child.set('isPassive', false);
            }
            function defaultTasks(child) {
                child.set('isCancelled', false);
            }
        }, 1000);
    },

    paste: function () {
        var task = this.rec;
        console.log(this.copiedTasks);

        if (task) {
            this.addTaskBelow(this.copiedTasks);
        }
        else {
            this.grid.taskStore.getRootNode().appendChild(this.copiedTasks);
        }
        this.copiedTasks = this.copyTaskWithChildren(this.copiedTasks);
    },

    copy: function () {
        var me = this;
        var tasks = this.grid.getSelectionModel().selected;
        tasks.each(function (t) {
            if (!tasks.contains(t.parentNode)) {
                me.copiedTasks = me.copyTaskWithChildren(t);
            }
        });
    },

    cut: function () {
        var me = this;
        var tasks = this.grid.getSelectionModel().selected;
        tasks.each(function (t) {
            if (!tasks.contains(t.parentNode)) {
                me.copiedTasks = me.copyTaskWithChildren(t);
                me.grid.taskStore.remove(tasks.getRange());
            }
        });
    },

    lock: function () {
        var task = this.rec;
        if (task.data.Lock == 0) {
            task.set('Lock', 1);
        } else {
            task.set('Lock', 0);
        }
    },

    copyTaskWithChildren: function (original) {
        var me = this;
        var model = this.grid.getTaskStore().model;

        var newTask = new model({
        });

        newTask.set('leaf', (original && original.get('leaf')) || null);
        newTask.set('expanded', (original && original.get('expanded')) || null);
        newTask.setName((original && original.getName()) || null);
        newTask.set(newTask.startDateField, (original && original.getStartDate()) || null);
        newTask.set(newTask.endDateField, (original && original.getEndDate()) || null);
        newTask.set(newTask.percentDoneField, (original && original.getPercentDone()) || null);
        newTask.set(newTask.durationField, (original && original.getDuration()) || null);
        newTask.set(newTask.durationUnitField, (original && original.getDurationUnit()) || 'd');
        newTask.set(newTask.effortField, (original && original.getEffort()) || null);
        newTask.set(newTask.effortUnitField, (original && original.getEffortUnit()) || 'h');
        newTask.set(newTask.schedulingModeField, (original && original.getSchedulingMode()) || 'Normal');
        newTask.set(newTask.clsField, (original && original.getCls()) || null);
        //newTask.set('parentId', (original && original.get('parentId')) || null);
        //newTask.set('Priority', (original && original.get('Priority')) || null);
        //newTask.set('index', (original && original.get('index')) || null);
        newTask.set('isCritical', (original && original.get('isCritical')) || null);
        newTask.set('isClarificationRequired', (original && original.get('isClarificationRequired')) || null);
        newTask.set('LimitDate', (original && original.get('LimitDate')) || null);
        newTask.set('isNotice', (original && original.get('isNotice')) || null);
        newTask.set('isReshow', (original && original.get('isReshow')) || null);
        newTask.set('isCompleted', (original && original.get('isCompleted')) || null);
        newTask.set('isPassive', (original && original.get('isPassive')) || null);
        newTask.set('isCancelled', (original && original.get('isCancelled')) || null);
        newTask.set('Act_Fixed_Cost', (original && original.get('Act_Fixed_Cost')) || null);
        newTask.set('Contact', (original && original.get('Contact')) || null);
        newTask.set('Responsible', (original && original.get('Responsible')) || null);
        newTask.set('Resource_Names', (original && original.get('Resource_Names')) || null);
        newTask.set('Created', (original && original.get('Created')) || null);
        newTask.set('Reshow_Date', (original && original.get('Reshow_Date')) || null);
        newTask.set('Capacity_usage', (original && original.get('Capacity_usage')) || null);
        newTask.set('Actual_Start', (original && original.get('Actual_Start')) || null);
        newTask.set('Object', (original && original.get('Object')) || null);
        newTask.set('Actual_Finish', (original && original.get('Actual_Finish')) || null);
        newTask.set('Rem_Time', (original && original.get('Rem_Time')) || null);
        newTask.set('Completed', (original && original.get('Completed')) || null);
        newTask.set('Actual_Duration', (original && original.get('Actual_Duration')) || null);
        newTask.set('Duration_Act_div_Pld', (original && original.get('Duration_Act_div_Pld')) || null);
        newTask.set('Duration_Act_Pld', (original && original.get('Duration_Act_Pld')) || null);
        newTask.set('Req_Effort', (original && original.get('Req_Effort')) || null);
        newTask.set('Req_Ress', (original && original.get('Req_Ress')) || null);
        newTask.set('Effort', (original && original.get('Effort')) || null);
        newTask.set('Actual_Effort', (original && original.get('Actual_Effort')) || null);
        newTask.set('Effort_Pld_Req', (original && original.get('Effort_Pld_Req')) || null);
        newTask.set('Effort_Pld_div_Req', (original && original.get('Effort_Pld_div_Req')) || null);
        newTask.set('Effort_Act_Pld1', (original && original.get('Effort_Act_Pld1')) || null);
        newTask.set('Effort_Act_Pld', (original && original.get('Effort_Act_Pld')) || null);
        newTask.set('Ovt_Effort', (original && original.get('Ovt_Effort')) || null);
        newTask.set('Actual_Ovt_Effort', (original && original.get('Actual_Ovt_Effort')) || null);
        newTask.set('Fixed_Cost', (original && original.get('Fixed_Cost')) || null);
        newTask.set('Standard_Rate', (original && original.get('Standard_Rate')) || null);
        newTask.set('Act_Standard_Rate', (original && original.get('Act_Standard_Rate')) || null);
        newTask.set('Labor_Cost', (original && original.get('Labor_Cost')) || null);
        newTask.set('Act_Labor_Cost', (original && original.get('Act_Labor_Cost')) || null);
        newTask.set('Ovt_Cost', (original && original.get('Ovt_Cost')) || null);
        newTask.set('Act_Ovt_Cost', (original && original.get('Act_Ovt_Cost')) || null);
        newTask.set('Total_Cost', (original && original.get('Total_Cost')) || null);
        newTask.set('Act_Total_Cost', (original && original.get('Act_Total_Cost')) || null);
        newTask.set('Total_Cost_Act_div_Pld', (original && original.get('Total_Cost_Act_div_Pld')) || null);
        newTask.set('Total_Cost_Act_Pld', (original && original.get('Total_Cost_Act_Pld')) || null);
        newTask.set('Cost_Calc_Meth', (original && original.get('Cost_Calc_Meth')) || null);
        newTask.set('Time_Unit_of_Cost', (original && original.get('Time_Unit_of_Cost')) || null);
        newTask.set('Time_Unit_of_Effort', (original && original.get('Time_Unit_of_Effort')) || null);
        newTask.set('Time_Unit_of_Duration', (original && original.get('Time_Unit_of_Duration')) || null);
        newTask.set('Text1', (original && original.get('Text1')) || null);
        newTask.set('Text2', (original && original.get('Text2')) || null);
        newTask.set('Text3', (original && original.get('Text3')) || null);
        newTask.set('Text4', (original && original.get('Text4')) || null);
        newTask.set('Text5', (original && original.get('Text5')) || null);
        newTask.set('Text6', (original && original.get('Text6')) || null);
        newTask.set('Text7', (original && original.get('Text7')) || null);
        newTask.set('Text8', (original && original.get('Text8')) || null);
        newTask.set('Text9', (original && original.get('Text9')) || null);
        newTask.set('Text10', (original && original.get('Text10')) || null);
        newTask.set('Number1', (original && original.get('Number1')) || null);
        newTask.set('Number2', (original && original.get('Number2')) || null);
        newTask.set('Number3', (original && original.get('Number3')) || null);
        newTask.set('Number4', (original && original.get('Number4')) || null);
        newTask.set('Number5', (original && original.get('Number5')) || null);
        newTask.set('Number6', (original && original.get('Number6')) || null);
        newTask.set('Number7', (original && original.get('Number7')) || null);
        newTask.set('Number8', (original && original.get('Number8')) || null);
        newTask.set('Number9', (original && original.get('Number9')) || null);
        newTask.set('Number10', (original && original.get('Number10')) || null);
        newTask.set('Finished_pr', (original && original.get('Finished_pr')) || null);
        newTask.set('End_progn', (original && original.get('End_progn')) || null);
        newTask.set('Dur_progn', (original && original.get('Dur_progn')) || null);
        newTask.set('Work_progn', (original && original.get('Work_progn')) || null);
        newTask.set('EMail', (original && original.get('EMail')) || null);
        newTask.set('Qty_Pld', (original && original.get('Qty_Pld')) || null);
        newTask.set('Qty_durch_Time_Pld', (original && original.get('Qty_durch_Time_Pld')) || null);
        newTask.set('Qty_Act', (original && original.get('Qty_Act')) || null);
        newTask.set('Qty_durch_Time_Act', (original && original.get('Qty_durch_Time_Act')) || null);
        newTask.set('Factor', (original && original.get('Factor')) || null);
        newTask.set('Buffer', (original && original.get('Buffer')) || null);
        newTask.set('Buffer_cum', (original && original.get('Buffer_cum')) || null);
        newTask.set('Buffer_Act', (original && original.get('Buffer_Act')) || null);
        newTask.set('Buffer_Act_cum', (original && original.get('Buffer_Act_cum')) || null);
        newTask.set('Constraint_Type', (original && original.get('Constraint_Type')) || null);
        newTask.set('Successors', (original && original.get('Successors')) || null);
        newTask.set('Group_Name', (original && original.get('Group_Name')) || null);
        //newTask.set('Ord_ID', (original && original.get('Ord_ID')) || null);
        //newTask.set('Pro_ID', (original && original.get('Pro_ID')) || null);
        //newTask.set('Vor_ID', (original && original.get('Vor_ID')) || null);
        //newTask.set('Ord_ID2', (original && original.get('Ord_ID2')) || null);
        //newTask.set('Pro_ID2', (original && original.get('Pro_ID2')) || null);
        //newTask.set('Vor_ID2', (original && original.get('Vor_ID2')) || null);
        newTask.set('Rec_Options', (original && original.get('Rec_Options')) || null);
        //newTask.set('Outline_Level', (original && original.get('Outline_Level')) || null);
        newTask.set('NoticeStatus', (original && original.get('NoticeStatus')) || null);
        newTask.set('Auftr_ID', (original && original.get('Auftr_ID')) || null);
        newTask.set('Bearb_ID', (original && original.get('Bearb_ID')) || null);
        newTask.set('LastUserMod', (original && original.get('LastUserMod')) || null);
        newTask.set('LastModification', (original && original.get('LastModification')) || null);
        newTask.set('LastUser', (original && original.get('LastUser')) || null);

        original.eachChild(function (t) {
            newTask.addSubtask(me.copyTaskWithChildren(t));
        });

        return newTask;
    },

    // actions follow below
    deleteTask: function () {
        var e = this.grid.getSelectionModel().selected;
        this.grid.taskStore.remove(e.getRange());
    },

    editLeftLabel: function () {
        this.grid.getSchedulingView().editLeftLabel(this.rec);
    },

    editRightLabel: function () {
        this.grid.getSchedulingView().editRightLabel(this.rec);
    },

    converToMilestone: function () {
        var task = this.rec;
        task.setStartDate(task.getEndDate(), false);
    },

    converToRegularTask: function () {
        var task = this.rec;
        task.set({
            StartDate: task.calculateStartDate(task.getStartDate(), 1, Sch.util.Date.DAY),
            EndDate: task.getStartDate(),
            Duration: 1,
            DurationUnit: Sch.util.Date.DAY
        });
    },

    /**
     * Handler for the "add task above" menu item
     */
    addTaskAboveAction: function () {
        this.addTaskAbove(this.copyTask(this.rec));
    },

    /**
     * Handler for the "add task below" menu item
     */
    addTaskBelowAction: function () {
        this.addTaskBelow(this.copyTask(this.rec));
    },

    OnLevel1Action: function (newTask) {
        var tasks = this.grid.getSelectionModel().selected;
        newTask.Outline_Level = 1;
        newTask.leaf = true;
        newTask.Name = '(Folder desig.)';
        newTask.isCriticalTask = false;
        newTask.isClarificationRequired = false;
        newTask.isPassive = false;
        newTask.isCancelled = false;
        newTask.isNotice = false;
        newTask.isCompleted = false;
        newTask.isReshow = false;
        newTask.NoticeStatus = 3;
        newTask.LastModification = new Date();
        newTask.LastUserMod = new Date();
        newTask.Group_Name = newTask.Name;
        newTask.Created = new Date();
        if (tasks.items.length > 0) {
            newTask.parentId = tasks.items[0].data.Id;
        } else {
            newTask.parentId = 0;
        }
        this.grid.taskStore.getRootNode().appendChild(newTask);
    },

    OnLevel2Action: function (newTask) {
        var task = this.rec;

        if (task.data.Outline_Level == 1) {
            newTask.Outline_Level = 2;
            newTask.leaf = true;
            newTask.Name = '(Project desig.)';
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.Name;
            newTask.Created = new Date();
            task.set('leaf', false);
            task.set('Object', parseInt(task.data.Object) + 1);
            task.appendChild(newTask);
            task.expand();
        }
        else if (task.data.Outline_Level == 2) {
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.data.Name;
            newTask.Created = new Date();
            newTask.Outline_Level = 2;
            newTask.Name = '(Project desig.)';
            newTask.leaf = true;
            task.parentNode.set('Object', parseInt(task.parentNode.data.Object) + 1);
            if (task) {
                task.parentNode.insertBefore(newTask, task.nextSibling);
            } else {
                this.grid.taskStore.getRootNode().appendChild(newTask);
            }
        } else if (task.data.Outline_Level >= 3) {
            var parentTask;
            findRootParentNode(task);
            function findRootParentNode(t) {
                if (t.parentNode.data.Id > 0) {
                    findRootParentNode(t.parentNode);
                } else {
                    parentTask = t;
                }
            }
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.data.Name;
            newTask.Created = new Date();
            newTask.Outline_Level = 2;
            newTask.Name = '(Project desig.)';
            newTask.leaf = true;
            parentTask.set('Object', parseInt(parentTask.data.Object) + 1);
            parentTask.appendChild(newTask);
        }
    },

    OnLevel3Action: function (newTask) {
        var task = this.rec;
        if (task.data.Outline_Level == 2) {
            newTask.Outline_Level = 3;
            newTask.leaf = true;
            newTask.Name = '(Task desig.)';
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.data.Name;
            newTask.Created = new Date();
            task.set('leaf', false);
            task.set('Object', parseInt(task.data.Object) + 1);
            task.appendChild(newTask);
            task.expand();
        }
        else if (task.data.Outline_Level >= 3) {
            var parentTask;
            findRootParentNode(task);
            function findRootParentNode(t) {
                if (t.parentNode.data.Outline_Level >= 2) {
                    findRootParentNode(t.parentNode);
                } else {
                    parentTask = t;
                }
            }
            newTask.Outline_Level = 3;
            newTask.leaf = true;
            newTask.Name = '(Task desig.)';
            newTask.isCriticalTask = false;
            newTask.isClarificationRequired = false;
            newTask.isPassive = false;
            newTask.isCancelled = false;
            newTask.isNotice = false;
            newTask.isCompleted = false;
            newTask.isReshow = false;
            newTask.NoticeStatus = 3;
            newTask.LastModification = new Date();
            newTask.LastUserMod = new Date();
            newTask.Group_Name = task.parentNode.data.Name;
            newTask.Created = new Date();
            parentTask.set('leaf', false);
            parentTask.set('Object', parseInt(parentTask.data.Object) + 1);
            parentTask.appendChild(newTask);
            parentTask.expand();
        }

    },

    /**
     * Handler for the "add subtask" menu item
     */
    addSubtask: function (newTask) {
        var task = this.rec;
        newTask.leaf = true;
        newTask.parentId = task.data.Id;
        newTask.Outline_Level = task.data.Outline_Level + 1;
        if (task.data.Outline_Level == 1) {
            newTask.Name = '(Project desig.)';
        }
        else if (task.data.Outline_Level >= 2) {
            newTask.Name = '(Task desig.)';
        }
        newTask.isCriticalTask = false;
        newTask.isClarificationRequired = false;
        newTask.isPassive = false;
        newTask.isCancelled = false;
        newTask.isNotice = false;
        newTask.isCompleted = false;
        newTask.isReshow = false;
        newTask.NoticeStatus = 3;
        newTask.LastModification = new Date();
        newTask.LastUserMod = new Date();
        newTask.Group_Name = task.parentNode.data.Name;
        newTask.Created = new Date();
        task.set('leaf', false);
        task.set('Object', parseInt(task.data.Object) + 1);
        task.appendChild(newTask);
        task.expand();
    },

    /**
     * Handler for the "add successor" menu item
     */
    addSuccessor: function () {
        var task = this.rec,
            depStore = this.grid.dependencyStore,
            newTask = this.copyTask(task);

        this.addTaskBelow(newTask);
        newTask.set('StartDate', task.getEndDate());
        newTask.set('NoticeStatus', '3');
        if (task.data.Outline_Level == 2) {
            newTask.Name = '(Project desig.)';
        }
        else if (task.data.Outline_Level > 2) {
            newTask.Name = '(Task desig.)';
        }
        newTask.setDuration(1, Sch.util.Date.DAY);
        //task.parentNode.set('Object', parseInt(task.parentNode.data.Object) + 1);
        var dependencyClass = depStore.model;

        depStore.add(
            new dependencyClass({
                From: task.getId() || task.internalId,
                To: newTask.getId() || newTask.internalId,
                Type: dependencyClass.EndToStart
            })
        );
    },

    /**
     * Handler for the "add predecessor" menu item
     */
    addPredecessor: function () {
        var task = this.rec;
        var depStore = this.grid.dependencyStore;
        var newTask = this.copyTask(task);
        this.addTaskAbove(newTask);
        newTask.set('NoticeStatus', '3');
        if (task.data.Outline_Level == 1) {
            newTask.set('Name', '(Folder desig.)');
        }
        else if (task.data.Outline_Level == 2) {
            newTask.set('Name', '(Project desig.)');
        }
        else if (task.data.Outline_Level > 2) {
            newTask.set('Name', '(Task desig.)');
        }
        newTask.set({
            StartDate: newTask.calculateStartDate(task.getStartDate(), 1, Sch.util.Date.DAY),
            EndDate: task.getStartDate(),
            Duration: 1,
            DurationUnit: Sch.util.Date.DAY
        });

        var dependencyClass = depStore.model;

        depStore.add(
            new dependencyClass({
                From: newTask.getId() || newTask.internalId,
                To: task.getId() || task.internalId,
                Type: dependencyClass.EndToStart
            })
        );
    },

    /**
     * Handler for the "add milestone" menu item
     */
    addMilestone: function () {
        var task = this.rec,
            newTask = this.copyTask(task);

        this.addTaskBelow(newTask);
        if (task.data.Outline_Level == 1) {
            newTask.Name = '(Project desig.)';
        }
        else if (task.data.Outline_Level >= 2) {
            newTask.Name = '(Task desig.)';
        }
        newTask.setStartDate(newTask.getEndDate(), false);
        newTask.set('NoticeStatus', '1');
    }
});
