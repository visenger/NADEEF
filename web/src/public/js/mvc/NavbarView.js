/*
 * QCRI, NADEEF LICENSE
 * NADEEF is an extensible, generalized and easy-to-deploy data cleaning platform built at QCRI.
 * NADEEF means "Clean" in Arabic
 *
 * Copyright (c) 2011-2013, Qatar Foundation for Education, Science and Community Development (on
 * behalf of Qatar Computing Research Institute) having its principle place of business in Doha,
 * Qatar with the registered address P.O box 5825 Doha, Qatar (hereinafter referred to as "QCRI")
 *
 * NADEEF has patent pending nevertheless the following is granted.
 * NADEEF is released under the terms of the MIT License, (http://opensource.org/licenses/MIT).
 */

define([
    "router",
    "state",
    "requester",
    "state",
    "text!mvc/template/navbar.template.html",
    "text!mvc/template/project.template.html",
    "mvc/HomeView"
], function(Router, State, Requester, State, NavbarTemplate, ProjectTemplate, HomeView) {
    function err(msg) {
        $('#projectModal-alert').html([
            ['<div class="alert alert-error">'],
            ['<button type="button" class="close" data-dismiss="alert">'],
            ['&times;</button>'],
            ['<span>' + msg + '</span></div>']].join(''));
    }

    function bindEvent() {
        $('#navbar').find("li a").on('click', function() {
            $('#navbar').find('.active').removeClass('active');
            $(this).parent.addClass('active');
        });

        $('#project-modal-close').on('click', function() {
            var oldProject = State.get('project');
            if (oldProject != null)
                $('#projectModal').modal('hide');
            else
                err("No project is selected.");
        });

        $("#refresh").on('click', HomeView.refresh);

        $("#change").on("click", function() {
            $('#projectModal').find('.modal-body').remove();
            Requester.getProject(function(data) {
                var projectList = _.flatten(data['data']);
                selectProject('projectModal', projectList);
            }, function() {
                console.log("Getting project failed.");
            });
        });

        $("#project-button").on("click", function() {
            var newProject = $("#create-new-project").val();
            if (newProject != null && newProject != "") {
                var pattern = new RegExp("^[a-zA-Z]\\w*");
                var match = pattern.exec(newProject);

                // regexp check failed.
                if (match != newProject) {
                    $("#project-input").addClass("error");
                    err("Input text has incorrect char.");
                    return;
                }
            }

            if (newProject != null && newProject != "") {
                Requester.createProject(newProject, function() {
                    $("#projectModal").modal('hide');
                    $("#projectName").text(newProject);
                    Router.redirect('#home', { name : newProject });
                });
            } else {
                var selectedProject = $("#select-existing-project").val();
                if (selectedProject == null) {
                    err("No project is selected.");
                    return;
                }

                enterProject(selectedProject);
            }
        });
    }
    
    function render() {
        $('body').append(_.template(NavbarTemplate)());
    }

    function selectProject(id, projectList) {
        var modalHtml = _.template(ProjectTemplate) ({projects: projectList});
        $('#' + id).find(".modal-footer").before(modalHtml);

        // event handler for modal
        $("#create-new-project").keypress(function() {
            $("#project-input").removeClass("error");
            $("#project-input").find("span").text("");
        });

        $('#' + id).modal('show');
    }

    function enterProject(selectedProject) {
        $("#projectModal").modal('hide');
        $("#projectName").text(selectedProject);
        State.set('project', selectedProject);
        Router.redirect('#home', { name : selectedProject });
    }

	function start() {
		render();
		bindEvent();

        // start project selection process
        Requester.getProject(function(data) {
            var projectList = _.flatten(data['data']);
            var oldProject = State.get('project');
            if (oldProject && _.indexOf(projectList, oldProject) > -1)
                enterProject(oldProject);
            else
                selectProject('projectModal', projectList);
        }, function() {
            console.log("Getting project failed.");
        });
	}
	
    return {
        start : start
    };
});
