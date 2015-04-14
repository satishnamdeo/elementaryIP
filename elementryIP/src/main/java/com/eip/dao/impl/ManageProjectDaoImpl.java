package com.eip.dao.impl;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.eip.dao.ManageProjectDao;
import com.eip.entity.ProjectManage;
import com.eip.entity.ProjectUser;

@Repository("ManageProjectDao")
@Transactional
public class ManageProjectDaoImpl implements ManageProjectDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public Integer createProject(ProjectManage projectManage) {
		/*System.out.println(" createProject dao :START ");*/
		Integer id = 0;
		Session session = sessionFactory.getCurrentSession();
		try {
			ValidatorFactory factory = Validation
					.buildDefaultValidatorFactory();
			Validator validator = factory.getValidator();

			Set<ConstraintViolation<ProjectManage>> constraintViolations = validator
					.validate(projectManage);

			if (constraintViolations.size() > 0) {
				/*System.out.println("Constraint Violations occurred..");*/
				for (ConstraintViolation<ProjectManage> contraints : constraintViolations) {
					System.out.println(contraints.getRootBeanClass()
							.getSimpleName()
							+ "."
							+ contraints.getPropertyPath()
							+ " "
							+ contraints.getMessage());
				}
			}
			id = (Integer) session.save(projectManage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*System.out.println(" createProject dao :END ");*/
		return id;
	}

	@Override
	public List<ProjectManage> getprojectList() {
	/*	System.out.println(" projectManageList dao : Start ");*/
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ProjectManage.class);
		List<ProjectManage> projectManageList = (List<ProjectManage>) criteria
				.list();
		/*System.out.println(" projectManageList size : END"
				+ projectManageList.size());*/
		return projectManageList;
	}

	@Override
	public Integer addCollaborator(ProjectUser collaborator) {
		/*System.out.println("In Colaborator dao "
				+ collaborator.getColabId().getUserId());*/
		Integer id = 0;
		try {
			Session session = sessionFactory.getCurrentSession();
			/*
			 * ValidatorFactory factory =
			 * Validation.buildDefaultValidatorFactory(); Validator validator =
			 * factory.getValidator();
			 * 
			 * Set<ConstraintViolation<ProjectCollaborator>>
			 * constraintViolations = validator.validate(collaborator);
			 * 
			 * if (constraintViolations.size() > 0 ) {
			 * System.out.println("Constraint Violations occurred.."); for
			 * (ConstraintViolation<ProjectCollaborator> contraints :
			 * constraintViolations) {
			 * System.out.println(contraints.getRootBeanClass().getSimpleName()+
			 * "." + contraints.getPropertyPath() + " " +
			 * contraints.getMessage()); } }
			 */
			session.save(collaborator);

		} catch (Exception e) {
			e.printStackTrace();
		}
		/*System.out.println("In Colaborator dao END " + id);*/
		return null;
	}

	@Override
	public void deleteProject(ProjectManage projectManage) {
		Session session = sessionFactory.getCurrentSession();
		try {
			session.delete(projectManage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public ProjectManage getProjectByProjectId(Integer projectId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ProjectManage.class);
		ProjectManage projectManage = (ProjectManage) criteria.add(
				Restrictions.eq("projectId", projectId)).uniqueResult();
		return projectManage;
	}

	@Override
	public List<ProjectUser> getprojectListByColabId(Integer userId) {
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ProjectUser.class);
		List<ProjectUser> projectUser = (List<ProjectUser>) criteria.add(
				Restrictions.eq("colabId.userId", userId)).list();
		return projectUser;

	}

	@Override
	public List<ProjectManage> getprojectListByUserId(Integer userId) {
		/*System.out.println(" projectManageList dao : Start ");*/
		Session session = sessionFactory.getCurrentSession();
		Criteria criteria = session.createCriteria(ProjectManage.class);
		List<ProjectManage> projectManageList = (List<ProjectManage>) criteria
				.add(Restrictions.eq("projectOwner.userId", userId)).list();
	/*	System.out.println(" projectManageList size : END"
				+ projectManageList.size());*/
		return projectManageList;

	}

}
